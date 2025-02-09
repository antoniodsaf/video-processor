package br.com.fiap.video.processor.application.port.inbound.process

import br.com.fiap.video.processor.application.port.inbound.process.dto.CreateFramesInboundRequest

interface ExtractFramesService {
    fun invoke(createFramesInboundRequest: CreateFramesInboundRequest)
}
