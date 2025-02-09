package br.com.fiap.video.processor.application.core.domain.valueobject

import br.com.fiap.video.processor.application.core.domain.exception.InvalidProcessIdException
import java.util.UUID

@JvmInline
value class ProcessId private constructor(val value: String) {
    companion object {
        fun new(value: String): Result<ProcessId> {
            val uuid = runCatching { UUID.fromString(value) }
                .getOrElse { return Result.failure(InvalidProcessIdException("invalid process id.")) }

            return Result.success(ProcessId(uuid.toString()))
        }

        fun generate() = ProcessId(UUID.randomUUID().toString())
    }
}