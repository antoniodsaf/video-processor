package br.com.fiap.video.processor.adapter.outbound.aws.s3.client

import br.com.fiap.video.processor.util.convertMultiPartToFile
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths


@Component
class DefaultS3FileTransfer(private val s3Client: S3Client): S3FileTransfer {

    companion object {
        private const val OUTPUT_DOWNLOAD_DIR = "/tmp/download"
    }

    override fun upload(bucketName: String, file: MultipartFile, key: String) {
        this.uploadFile(bucketName, convertMultiPartToFile(file), key)
    }

    override fun uploadFile(bucketName: String, file: File, key: String) {
        try {
            val putObjectRequest: PutObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
            s3Client.putObject(putObjectRequest, file.toPath())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun downloadFile(bucketName: String, key: String): File {
        try {
            val outputPath = Paths.get("$OUTPUT_DOWNLOAD_DIR/$key")
            if (outputPath.toFile().exists()) {
                return outputPath.toFile()
            }

            download(bucketName, key).let {
                Files.copy(it, outputPath)
            }
            return File(outputPath.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return File("")
    }

    override fun download(bucketName: String, key: String): InputStream {
        val request = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return s3Client.getObject(request)
    }
}