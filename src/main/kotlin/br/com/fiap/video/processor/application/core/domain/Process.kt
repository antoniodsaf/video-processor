package br.com.fiap.video.processor.application.core.domain

import br.com.fiap.video.processor.application.core.domain.valueobject.*
import java.time.LocalDateTime

class Process private constructor(
    val id: ProcessId,
    val originalFile: String,
    val user: String,
    val status: ProcessStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun new(
            id: ProcessId,
            originalFile: String,
            user: String,
            status: ProcessStatus,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
        ): Result<Process> = Result.success(Process(id, originalFile, user, status, createdAt, updatedAt))
    }
}
