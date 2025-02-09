package br.com.fiap.video.processor.application.port.outbound.process

import br.com.fiap.video.processor.application.port.outbound.process.dto.ProcessMessage

fun interface ProcessSentMessenger {
    fun send(processSentMessage: ProcessMessage)
}