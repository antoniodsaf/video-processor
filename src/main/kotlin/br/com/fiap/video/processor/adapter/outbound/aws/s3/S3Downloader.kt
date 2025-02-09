package br.com.fiap.video.processor.adapter.outbound.aws.s3

import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.S3FileTransfer
import br.com.fiap.video.processor.application.port.outbound.process.FileDownloader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class S3Downloader(
    @Value("\${spring.cloud.aws.s3.processing-bucket}")
    private val bucketName: String,
    private val s3File: S3FileTransfer,
) : FileDownloader {
    override fun download(key: String): InputStream {
        return s3File.download(bucketName, key)
    }
}