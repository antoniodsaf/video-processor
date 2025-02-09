package br.com.fiap.video.processor.application.mapper.process

import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessId
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessStatus
import br.com.fiap.video.processor.application.port.outbound.process.dto.ProcessOutboundResponse
import java.time.LocalDateTime

class DefaultProcessDomainMapper : ProcessMapper {

    override fun new(
        id: ProcessId,
        originalFile: String,
        user: String,
        status: ProcessStatus,
        createdAt: LocalDateTime,
        updatedAt: LocalDateTime
    ): Result<br.com.fiap.video.processor.application.core.domain.Process> {
        return br.com.fiap.video.processor.application.core.domain.Process.new(
            id = id,
            originalFile = originalFile,
            user = user,
            status = status,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }

    override fun new(processOutboundResponse: ProcessOutboundResponse): Result<br.com.fiap.video.processor.application.core.domain.Process> =
        with(processOutboundResponse) {
            val id = ProcessId.new(id).getOrElse { return Result.failure(it) }
            val status = ProcessStatus.findByValue(status).getOrElse { return Result.failure(it) }

            return br.com.fiap.video.processor.application.core.domain.Process.new(
                id = id,
                originalFile = originalFile,
                user = user,
                status = status,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }

}