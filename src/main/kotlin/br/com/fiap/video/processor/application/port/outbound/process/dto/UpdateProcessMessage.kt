package br.com.fiap.video.processor.application.port.outbound.process.dto

import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessStatus

data class UpdateProcessMessage(
    val id: String,
    val status: ProcessStatus,
)