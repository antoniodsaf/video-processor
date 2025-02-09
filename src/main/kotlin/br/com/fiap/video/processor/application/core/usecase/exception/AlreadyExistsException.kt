package br.com.fiap.video.processor.application.core.usecase.exception

open class AlreadyExistsException(val type: String, message: String) : Exception(message)
