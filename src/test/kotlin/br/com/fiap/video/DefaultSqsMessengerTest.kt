package br.com.fiap.video

import br.com.fiap.video.processor.adapter.outbound.aws.sqs.client.DefaultSqsMessenger
import io.awspring.cloud.sqs.operations.SqsTemplate
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.messaging.support.GenericMessage

class DefaultSqsMessengerTest {

    private val sqsTemplate: SqsTemplate = mockk(relaxed = true)
    private val defaultSqsMessenger = DefaultSqsMessenger(sqsTemplate)

    @Test
    fun `test send`() {
        val queueName = "test-queue"
        val message = "test message"

        every { sqsTemplate.send(queueName, any<GenericMessage<String>>()) } returns mockk()

        defaultSqsMessenger.send(queueName, message)

        verify { sqsTemplate.send(queueName, any<GenericMessage<String>>()) }
    }
}