package br.com.fiap.video.processor.infrastructure.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
class AWSConfiguration {

    @Value("\${spring.cloud.aws.region}")
    private lateinit var region: String

    @Value("\${spring.cloud.aws.endpoint:http://localhost:4566}")
    private lateinit var localEndpoint: String

    @Bean
    @Profile("local")
    fun sqsClientLocal(): SqsClient {
        return SqsClient.builder()
            .endpointOverride(URI.create(localEndpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsSessionCredentials.create(
                        accessKey,
                        secretKey,
                        sessionToken
                    )
                )
            )
            .region(Region.of(region))
            .build()
    }

    @Bean
    @Profile("!local && !test")
    fun sqsClientCloud(): SqsClient {
        return SqsClient.builder().build()
    }

    @Bean
    @Profile("!local && !test")
    fun sqsAsyncClientCloud(): SqsAsyncClient {
        return SqsAsyncClient.builder().credentialsProvider(
            StaticCredentialsProvider.create(
                AwsSessionCredentials.create(
                    accessKey,
                    secretKey,
                    sessionToken
                )
            )
        ).build()
    }

    @Bean
    @Profile("local")
    fun sqsAsyncClientLocal(): SqsAsyncClient {
        return SqsAsyncClient.builder()
            .endpointOverride(URI.create(localEndpoint))
            .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsSessionCredentials.create(
                    accessKey,
                    secretKey,
                    sessionToken
                )
            )
        ).build()
    }

    @Bean
    @Profile("local")
    fun s3ClientLocal(): S3Client {
        return S3Client.builder()
            .endpointOverride(URI.create(localEndpoint))
            .region(Region.of(region))
            .build()
    }

    @Value("\${aws.access.key}")
    private lateinit var accessKey: String

    @Value("\${aws.access.secret-key}")
    private lateinit var secretKey: String

    @Value("\${aws.access.session-token}")
    private lateinit var sessionToken: String

    @Bean
    @Profile("!local && !test")
    fun s3ClientCloud(): S3Client {
        val endpoint = S3Client.serviceMetadata().endpointFor(Region.of(region));
        val build = S3Client.builder()
            .endpointOverride(URI.create("https://" + endpoint.path))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsSessionCredentials.create(
                        accessKey,
                        secretKey,
                        sessionToken
                    )
                )
            )
            .region(Region.of(region))
            .build()
        return build
    }

}