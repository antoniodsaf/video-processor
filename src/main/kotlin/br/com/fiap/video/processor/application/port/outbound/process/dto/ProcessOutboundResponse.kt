package br.com.fiap.video.processor.application.port.outbound.process.dto

import java.time.LocalDateTime

data class ProcessOutboundResponse(
    val id: String,
    val originalFile: String,
    val user: String,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)