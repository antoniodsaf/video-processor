package br.com.fiap.video

import br.com.fiap.video.processor.adapter.outbound.aws.s3.S3Downloader
import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.S3FileTransfer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.io.ByteArrayInputStream
import java.io.InputStream

class S3DownloaderTest {

    private val s3FileTransfer: S3FileTransfer = mockk()
    private val s3Downloader = S3Downloader("test-bucket", s3FileTransfer)

    @Test
    fun `test download`() {
        val bucketName = "test-bucket"
        val key = "test-key"
        val content = "file content"
        val inputStream: InputStream = ByteArrayInputStream(content.toByteArray())

        every { s3FileTransfer.download(bucketName, key) } returns inputStream

        val result = s3Downloader.download(key)

        assertNotNull(result)
        assertTrue(result is InputStream)
        assertEquals(content, result.bufferedReader().use { it.readText() })
    }
}