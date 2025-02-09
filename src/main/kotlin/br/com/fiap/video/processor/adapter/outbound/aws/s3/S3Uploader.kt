package br.com.fiap.video.processor.adapter.outbound.aws.s3

import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.S3FileTransfer
import br.com.fiap.video.processor.application.port.outbound.process.FileUploader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class S3Uploader(
    @Value("\${spring.cloud.aws.s3.processing-bucket}")
    private val bucketName: String,
    private val s3File: S3FileTransfer,
) : FileUploader {
    override fun upload(file: MultipartFile, key: String) {
        s3File.upload(bucketName, file, key)
    }
}