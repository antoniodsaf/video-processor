package br.com.fiap.video

import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessId
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessStatus
import br.com.fiap.video.processor.application.mapper.process.DefaultProcessDomainMapper
import br.com.fiap.video.processor.application.port.outbound.process.dto.ProcessOutboundResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DefaultProcessDomainMapperTest {

    private val mapper = DefaultProcessDomainMapper()

    @Test
    fun `test new with individual parameters`() {
        val id = ProcessId.generate()
        val originalFile = "test-file"
        val user = "test-user"
        val status = ProcessStatus.IN_PROGRESS
        val createdAt = LocalDateTime.now()
        val updatedAt = LocalDateTime.now()

        val result = mapper.new(id, originalFile, user, status, createdAt, updatedAt)

        assertTrue(result.isSuccess)
        val process = result.getOrThrow()
        assertEquals(id, process.id)
        assertEquals(originalFile, process.originalFile)
        assertEquals(user, process.user)
        assertEquals(status, process.status)
        assertEquals(createdAt, process.createdAt)
        assertEquals(updatedAt, process.updatedAt)
    }

    @Test
    fun `test new with ProcessOutboundResponse`() {
        val processOutboundResponse = ProcessOutboundResponse(
            id = ProcessId.generate().value,
            originalFile = "test-file",
            user = "test-user",
            status = ProcessStatus.RECEIVED.value,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val result = mapper.new(processOutboundResponse)

        assertTrue(result.isSuccess)
        val process = result.getOrThrow()
        assertEquals(processOutboundResponse.id, process.id.value)
        assertEquals(processOutboundResponse.originalFile, process.originalFile)
        assertEquals(processOutboundResponse.user, process.user)
        assertEquals(ProcessStatus.RECEIVED, process.status)
        assertEquals(processOutboundResponse.createdAt, process.createdAt)
        assertEquals(processOutboundResponse.updatedAt, process.updatedAt)
    }
}