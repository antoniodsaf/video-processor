package br.com.fiap.video

import br.com.fiap.video.processor.application.core.usecase.exception.NotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NotFoundExceptionTest {

    @Test
    fun `test NotFoundException properties`() {
        val type = "Video"
        val message = "Video not found"
        val exception = NotFoundException(type, message)

        assertEquals(type, exception.type)
        assertEquals(message, exception.message)
    }
}