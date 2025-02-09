package br.com.fiap.video.processor.application.port.outbound.process

import org.springframework.web.multipart.MultipartFile

fun interface FileUploader {
    fun upload(file: MultipartFile, key: String)
}