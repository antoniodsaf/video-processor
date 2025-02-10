package br.com.fiap.video

import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.S3FileTransfer
import br.com.fiap.video.processor.application.core.usecase.process.ExtractFramesUseCase
import br.com.fiap.video.processor.application.port.outbound.process.UpdateProcessStatusSentMessenger
import br.com.fiap.video.processor.infrastructure.instantiation.ProcessPortsInstantiationConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Value

@ExtendWith(MockitoExtension::class)
class ProcessPortsInstantiationConfigTest {

    @Mock
    private lateinit var processUpdateStatusSentMessenger: UpdateProcessStatusSentMessenger

    @Mock
    private lateinit var s3File: S3FileTransfer

    private val bucketName: String = "test-bucket"

    @InjectMocks
    private lateinit var processPortsInstantiationConfig: ProcessPortsInstantiationConfig

    @Test
    fun `test extractFramesService bean instantiation`() {
        val extractFramesService = processPortsInstantiationConfig.extractFramesService(
            processUpdateStatusSentMessenger,
            s3File,
            bucketName
        )
        assertNotNull(extractFramesService)
        assertTrue(extractFramesService is ExtractFramesUseCase)
    }
}