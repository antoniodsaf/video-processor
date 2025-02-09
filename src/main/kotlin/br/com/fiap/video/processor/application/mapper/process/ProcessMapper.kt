package br.com.fiap.video.processor.application.mapper.process

import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessId
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessStatus
import br.com.fiap.video.processor.application.port.outbound.process.dto.ProcessOutboundResponse
import java.time.LocalDateTime

interface ProcessMapper {
    fun new(
        id: ProcessId,
        originalFile: String,
        user: String,
        status: ProcessStatus,
        createdAt: LocalDateTime,
        updatedAt: LocalDateTime,
    ): Result<br.com.fiap.video.processor.application.core.domain.Process>

    fun new(processOutboundResponse: ProcessOutboundResponse): Result<br.com.fiap.video.processor.application.core.domain.Process>
}
