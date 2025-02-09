package br.com.fiap.video.processor.application.core.usecase.process.exception

import br.com.fiap.video.processor.application.core.usecase.exception.InvalidDomainStateException

class InvalidProcessStateException(message: String) : InvalidDomainStateException(TYPE, message) {
    companion object {
        private const val TYPE = "Process"
    }
}
