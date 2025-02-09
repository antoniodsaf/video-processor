package br.com.fiap.video.processor.adapter.outbound.aws.sqs.client

import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Component

@Component
class DefaultSqsMessenger(private val sqsTemplate: SqsTemplate) : SqsMessenger {

    override fun send(queueName: String, message: String) {
        sqsTemplate.send(queueName, GenericMessage(message))
    }
}