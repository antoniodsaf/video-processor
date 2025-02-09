package br.com.fiap.video.processor.infrastructure.instantiation

import br.com.fiap.video.processor.adapter.outbound.aws.s3.client.S3FileTransfer
import br.com.fiap.video.processor.application.core.usecase.process.ExtractFramesUseCase
import br.com.fiap.video.processor.application.mapper.process.DefaultProcessDomainMapper
import br.com.fiap.video.processor.application.mapper.process.ProcessMapper
import br.com.fiap.video.processor.application.port.inbound.process.ExtractFramesService
import br.com.fiap.video.processor.application.port.outbound.process.UpdateProcessStatusSentMessenger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProcessPortsInstantiationConfig {

    @Bean
    fun processDomainMapper(): ProcessMapper {
        return DefaultProcessDomainMapper()
    }

    @Bean
    fun extractFramesService(
        processUpdateStatusSentMessenger: UpdateProcessStatusSentMessenger,
        s3File: S3FileTransfer,
        @Value("\${spring.cloud.aws.s3.processing-bucket}")
        bucketName: String
    ): ExtractFramesService {
        return ExtractFramesUseCase(
            processUpdateStatusSentMessenger,
            s3File,
            bucketName,
        )
    }
}
