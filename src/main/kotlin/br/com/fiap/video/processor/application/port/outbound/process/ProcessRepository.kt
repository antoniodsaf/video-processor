package br.com.fiap.video.processor.application.port.outbound.process

import br.com.fiap.video.processor.application.core.domain.Process
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessId
import br.com.fiap.video.processor.application.port.outbound.process.dto.ProcessOutboundResponse

interface ProcessRepository {
    fun save(process: Process): ProcessOutboundResponse
    fun findById(processId: ProcessId): ProcessOutboundResponse?
    fun findByUser(user: String): List<ProcessOutboundResponse>
}
