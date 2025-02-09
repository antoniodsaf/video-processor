package br.com.fiap.video.processor.application.port.outbound.process

import java.io.InputStream

fun interface FileDownloader {
    fun download(key: String): InputStream
}