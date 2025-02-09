package br.com.fiap.video.processor.adapter.outbound.aws.s3.client

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream

interface S3FileTransfer {
    fun upload(bucketName: String, file: MultipartFile, key: String)
    fun download(bucketName: String, key: String): InputStream
    fun downloadFile(bucketName: String, key: String): File
    fun uploadFile(bucketName: String, file: File, key: String)
}