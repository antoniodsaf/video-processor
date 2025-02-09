package br.com.fiap.video.processor.application.core.usecase.process.exception

import br.com.fiap.video.processor.application.core.usecase.exception.NotFoundException

class ProcessNotFoundException(message: String) : NotFoundException(TYPE, message) {
    companion object {
        private const val TYPE = "Process"
    }
}
