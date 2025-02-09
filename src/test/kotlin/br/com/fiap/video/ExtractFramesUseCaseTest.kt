package br.com.fiap.video

import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.S3FileTransfer
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessId
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessStatus
import br.com.fiap.video.processor.application.core.usecase.process.ExtractFramesUseCase
import br.com.fiap.video.processor.application.port.inbound.process.dto.CreateFramesInboundRequest
import br.com.fiap.video.processor.application.port.outbound.process.UpdateProcessStatusSentMessenger
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.InputStream


class ExtractFramesUseCaseTest {

    private lateinit var processUpdateStatusSentMessenger: UpdateProcessStatusSentMessenger
    private lateinit var s3File: S3FileTransfer
    private lateinit var extractFramesUseCase: ExtractFramesUseCase

    private val bucketName = "test-bucket"

    @BeforeEach
    fun setUp() {
        processUpdateStatusSentMessenger = mockk(relaxed = true)
        s3File = mockk()
        extractFramesUseCase = ExtractFramesUseCase(processUpdateStatusSentMessenger, s3File, bucketName)
    }

    @Test
    fun `should process video successfully with mocked files`() = runBlocking {
        val processId = ProcessId.generate().value
        val request = CreateFramesInboundRequest(processId)
        val inputStream: InputStream = File("src/test/resources/samplevideo.mp4").inputStream()

        every { s3File.download(bucketName, processId) } returns inputStream
        every { s3File.uploadFile(any(), any(), any()) } returns Unit

        extractFramesUseCase.invoke(request)

        verify { processUpdateStatusSentMessenger.send(match { it.status == ProcessStatus.IN_PROGRESS }) }
        verify { processUpdateStatusSentMessenger.send(match { it.status == ProcessStatus.DONE }) }
    }

    @Test
    fun `should handle error during frame extraction with mocked files`() = runBlocking {
        val processId = ProcessId.generate().value
        val request = CreateFramesInboundRequest(processId)
        val inputStream: InputStream = mockk()

        every { s3File.download(bucketName, processId) } returns inputStream
        every { inputStream.read(any<ByteArray>()) } returns -1
        every { s3File.uploadFile(any(), any(), any()) } throws RuntimeException("Frame extraction error")

        try {
            extractFramesUseCase.invoke(request)
        } catch (e: Exception) {
            // Expected exception
        }

        verify { processUpdateStatusSentMessenger.send(match { it.status == ProcessStatus.IN_PROGRESS }) }
        verify { processUpdateStatusSentMessenger.send(match { it.status == ProcessStatus.ERROR }) }
    }

    @Test
    fun `should handle error during zip compression with mocked files`() = runBlocking {
        val processId = ProcessId.generate().value
        val request = CreateFramesInboundRequest(processId)
        val inputStream: InputStream = mockk()

        every { s3File.download(bucketName, processId) } returns inputStream
        every { inputStream.read(any<ByteArray>()) } returns -1
        every { s3File.upload(any(), any(), any()) } throws RuntimeException("Zip compression error")

        try {
            extractFramesUseCase.invoke(request)
        } catch (e: Exception) {
            // Expected exception
        }

        verify { processUpdateStatusSentMessenger.send(match { it.status == ProcessStatus.IN_PROGRESS }) }
        verify { processUpdateStatusSentMessenger.send(match { it.status == ProcessStatus.ERROR }) }
    }
}