package br.com.fiap.video.processor.adapter.outbound.aws.sqs

import br.com.fiap.video.processor.adapter.outbound.aws.sqs.client.SqsMessenger
import br.com.fiap.video.processor.application.port.outbound.process.ProcessSentMessenger
import br.com.fiap.video.processor.application.port.outbound.process.dto.ProcessMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SqsProcessSentMessenger(
    @Value("\${spring.cloud.aws.sqs.processing-queue-name}")
    private val queueName: String,
    private val sqsMessenger: SqsMessenger
) : ProcessSentMessenger {

    override fun send(processSentMessage: ProcessMessage) {
        sqsMessenger.send(queueName, jacksonObjectMapper().writeValueAsString(processSentMessage))
    }
}