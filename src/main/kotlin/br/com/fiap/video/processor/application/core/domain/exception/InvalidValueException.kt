package br.com.fiap.video.processor.application.core.domain.exception

open class InvalidValueException(val type: String, message: String) : Exception(message)
