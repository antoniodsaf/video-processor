package br.com.fiap.video.processor.application.core.domain.valueobject

import br.com.fiap.video.processor.application.core.domain.exception.InvalidProcessStatusException

enum class ProcessStatus(val value: String) {
    RECEIVED("RECEIVED"),
    IN_PROGRESS("IN_PROGRESS"),
    ERROR("ERROR"),
    DONE("DONE"),
    ;

    companion object {
        fun findByValue(value: String): Result<ProcessStatus> {
            val orderStatus = entries.firstOrNull { it.value == value }
                ?: return Result.failure(InvalidProcessStatusException("Invalid process status: '$value'."))

            return Result.success(orderStatus)
        }
    }
}