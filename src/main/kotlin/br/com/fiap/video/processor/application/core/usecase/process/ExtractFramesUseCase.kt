package br.com.fiap.video.processor.application.core.usecase.process

import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.S3FileTransfer
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessId
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessStatus
import br.com.fiap.video.processor.application.port.inbound.process.ExtractFramesService
import br.com.fiap.video.processor.application.port.inbound.process.dto.CreateFramesInboundRequest
import br.com.fiap.video.processor.application.port.outbound.process.UpdateProcessStatusSentMessenger
import br.com.fiap.video.processor.application.port.outbound.process.dto.UpdateProcessMessage
import br.com.fiap.video.processor.util.EXTENSION_ZIP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.imageio.ImageIO

class ExtractFramesUseCase(
    private val processUpdateStatusSentMessenger: UpdateProcessStatusSentMessenger,
    private val s3File: S3FileTransfer,
    private val bucketName: String
) : ExtractFramesService {

    private class Folders(
        val outputFolder: File,
        val zipFolder: File
    )

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ExtractFramesUseCase::class.java)
        private const val FRAME_RATE = 1.0
        private const val OUTPUT_DIR = "frames"
        private const val ZIR_DIR = "zip"
        private const val EXTENSION_IMAGE = "png"
    }

    private fun createTemporaryFolders(): Folders {
        return Folders(
            Files.createTempDirectory(OUTPUT_DIR).toFile(),
            Files.createTempDirectory(ZIR_DIR).toFile()
        )
    }

    override fun invoke(createFramesInboundRequest: CreateFramesInboundRequest): Unit = runBlocking {
        ProcessId.new(createFramesInboundRequest.id).getOrThrow().let { processId ->
            logger.info("Processing video: ${processId.value}")
            val downloadedFile = s3File.download(bucketName, processId.value)
            val folders = createTemporaryFolders()
            val zipFilePath = "${folders.zipFolder}/${processId.value}.${EXTENSION_ZIP}"

            processUpdateStatusSentMessenger.send(
                UpdateProcessMessage(processId.value, ProcessStatus.IN_PROGRESS)
            )

            val imageFiles = withContext(Dispatchers.IO) {
                runCatching {
                    extractFrames(downloadedFile, folders.outputFolder)
                }.getOrElse { e ->
                    sendError(processId)
                    throw e
                }
            }

            withContext(Dispatchers.IO) {
                runCatching {
                    compressImagesToZip(imageFiles, zipFilePath)
                }.getOrElse { e ->
                    sendError(processId)
                    throw e
                }
            }

            processUpdateStatusSentMessenger.send(
                UpdateProcessMessage(processId.value, ProcessStatus.DONE)
            )
            folders.outputFolder.deleteRecursively()
            folders.zipFolder.deleteRecursively()
            logger.info("Success to process the file: ${processId.value}")
        }
    }

    private fun sendError(processId: ProcessId) {
        logger.error("Error to process the file: ${processId.value}")
        processUpdateStatusSentMessenger.send(
            UpdateProcessMessage(processId.value, ProcessStatus.ERROR)
        )
    }

    private fun compressImagesToZip(imageFiles: List<File>, zipFilePath: String) {
        val zipFile = File(zipFilePath)
        val zipOut = ZipOutputStream(FileOutputStream(zipFile))

        imageFiles.forEach { file ->
            zipOut.putNextEntry(ZipEntry(file.name))
            file.inputStream().use { it.copyTo(zipOut) }
            zipOut.closeEntry()
        }

        zipOut.close()
        s3File.uploadFile(bucketName, zipFile, zipFile.name)
        zipFile.delete()
    }

    private fun extractFrames(videoPath: InputStream, outputFolder: File): List<File> {
        if (!File(System.getProperty("java.io.tmpdir") + OUTPUT_DIR).exists())
            Files.createTempDirectory(OUTPUT_DIR)

        val grabber = FFmpegFrameGrabber(videoPath)
        grabber.start()

        val converter = Java2DFrameConverter()

        //TODO (made a progressing bar)
        // total frames in video
        val totalFrames = grabber.lengthInFrames

        val frameInterval = (grabber.frameRate / FRAME_RATE).toInt()
        var frameNumber = 0
        var extractedFrameNumber = 0
        val imageFiles = mutableListOf<File>()

        while (true) {
            val frame = grabber.grabImage() ?: break
            if (frameNumber % frameInterval == 0) {
                val image = converter.getBufferedImage(frame)
                val outputFile = Files.createTempFile(
                    outputFolder.toPath(),
                    "frame_${extractedFrameNumber.toString().padStart(5, '0')}",
                    ".$EXTENSION_IMAGE",
                ).toFile()
                ImageIO.write(image, EXTENSION_IMAGE, outputFile)
                imageFiles.add(outputFile)
                extractedFrameNumber++
            }
            frameNumber++
        }

        grabber.stop()
        return imageFiles
    }
}
