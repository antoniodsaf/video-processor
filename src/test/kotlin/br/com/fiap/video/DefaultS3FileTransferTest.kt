package br.com.fiap.video

import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.DefaultS3FileTransfer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths

class DefaultS3FileTransferTest {

    private val s3Client: S3Client = mockk()
    val fileName = "src/test/resources/samplevideo.mp4"
    val file = File(fileName)
    val fileInputStream = FileInputStream(file)
    val multipartFile = MockMultipartFile(
        "file", // nome do parâmetro no formulário
        fileName.split("/").last(), // nome do arquivo
        "text/plain", // tipo de conteúdo
        fileInputStream // InputStream do arquivo
    )
    private val getObjectResponse: GetObjectResponse = mockk()
    private val defaultS3FileTransfer = DefaultS3FileTransfer(s3Client)

    @Test
    fun `test uploadFile`() {
        val bucketName = "test-bucket"
        val key = "test-key"
        val file = File("test-file")

        every { s3Client.putObject(any<PutObjectRequest>(), file.toPath()) } returns mockk()

        defaultS3FileTransfer.uploadFile(bucketName, file, key)

        verify { s3Client.putObject(any<PutObjectRequest>(), file.toPath()) }
    }
}