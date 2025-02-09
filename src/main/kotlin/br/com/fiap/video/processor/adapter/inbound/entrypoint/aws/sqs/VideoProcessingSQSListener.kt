package br.com.fiap.video.processor.adapter.inbound.entrypoint.aws.sqs

import br.com.fiap.video.processor.application.port.inbound.process.ExtractFramesService
import br.com.fiap.video.processor.application.port.inbound.process.dto.CreateFramesInboundRequest
import br.com.fiap.video.processor.application.port.outbound.process.dto.ProcessMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class VideoProcessingSQSListener(
    private val extractFramesService: ExtractFramesService
) {

    @SqsListener("\${spring.cloud.aws.sqs.processing-queue-name}")
    fun receiveMessage(message: String) {
        val processMessage = jacksonObjectMapper()
            .readValue(message, ProcessMessage::class.java)
        extractFramesService.invoke(CreateFramesInboundRequest(processMessage.id))
    }

}

