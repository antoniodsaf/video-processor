package br.com.fiap.video.processor.application.port.outbound.process

import br.com.fiap.video.processor.application.port.outbound.process.dto.UpdateProcessMessage

fun interface UpdateProcessStatusSentMessenger {
    fun send(message: UpdateProcessMessage)
}