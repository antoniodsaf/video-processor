package br.com.fiap.video

import br.com.fiap.video.processor.adapter.outbound.aws.sqs.SqsProcessSentMessenger
import br.com.fiap.video.processor.adapter.outbound.aws.sqs.client.SqsMessenger
import br.com.fiap.video.processor.application.core.domain.valueobject.ProcessStatus
import br.com.fiap.video.processor.application.port.outbound.process.dto.ProcessMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class SqsProcessSentMessengerTest {

    private val queueName = "test-queue"
    private val sqsMessenger: SqsMessenger = mockk(relaxed = true)
    private val sqsProcessSentMessenger = SqsProcessSentMessenger(queueName, sqsMessenger)

    @Test
    fun `test send`() {
        val processMessage = ProcessMessage("test-id", "test-user", "file", ProcessStatus.IN_PROGRESS)
        val messageString = jacksonObjectMapper().writeValueAsString(processMessage)

        every { sqsMessenger.send(queueName, messageString) } returns Unit

        sqsProcessSentMessenger.send(processMessage)

        verify { sqsMessenger.send(queueName, messageString) }
    }
}