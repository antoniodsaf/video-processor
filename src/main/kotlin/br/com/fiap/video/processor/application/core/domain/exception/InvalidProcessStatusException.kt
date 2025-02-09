package br.com.fiap.video.processor.application.core.domain.exception

class InvalidProcessStatusException(message: String) : InvalidValueException(TYPE, message) {
    companion object {
        private const val TYPE = "ProcessStatus"
    }
}
