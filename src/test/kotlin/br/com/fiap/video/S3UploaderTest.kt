package br.com.fiap.video

import br.com.fiap.video.processor.adapter.outbound.aws.s3.S3Uploader
import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.S3FileTransfer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockMultipartFile
import java.io.File
import java.io.FileInputStream

@ExtendWith(MockitoExtension::class)
class S3UploaderTest {

    private val s3FileTransfer: S3FileTransfer = mockk()
    private val s3Uploader = S3Uploader("test-bucket", s3FileTransfer)

    @Test
    fun `test upload`() {
        val bucketName = "test-bucket"
        val key = "test-key"

        val fileName = "src/test/resources/samplevideo.mp4"
        val file = File(fileName)
        val fileInputStream = FileInputStream(file)

        val multipartFile = MockMultipartFile(
            "file", // nome do parâmetro no formulário
            fileName, // nome do arquivo
            "text/plain", // tipo de conteúdo
            fileInputStream // InputStream do arquivo
        )

        every { s3FileTransfer.upload(bucketName, multipartFile, key) } returns Unit

        s3Uploader.upload(multipartFile, key)

        verify { s3FileTransfer.upload(bucketName, multipartFile, key) }
    }
}