package br.com.fiap.video.processor.adapter.outbound.aws.sqs

import br.com.fiap.video.processor.adapter.outbound.aws.sqs.client.SqsMessenger
import br.com.fiap.video.processor.application.port.outbound.process.UpdateProcessStatusSentMessenger
import br.com.fiap.video.processor.application.port.outbound.process.dto.UpdateProcessMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SqsUpdateProcessStatusSentMessenger(
    @Value("\${spring.cloud.aws.sqs.update-process-status-queue-name}")
    private val queueName: String,
    private val sqsMessenger: SqsMessenger
) : UpdateProcessStatusSentMessenger {

    override fun send(message: UpdateProcessMessage) {
        sqsMessenger.send(queueName, jacksonObjectMapper().writeValueAsString(message))
    }
}