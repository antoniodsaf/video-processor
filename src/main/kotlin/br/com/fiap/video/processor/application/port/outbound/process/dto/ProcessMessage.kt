package br.com.fiap.video.processor.application.port.outbound.process.dto

import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessStatus

data class ProcessMessage(
    val id: String,
    val user: String,
    val originalFile: String,
    val status: ProcessStatus? = null,
)