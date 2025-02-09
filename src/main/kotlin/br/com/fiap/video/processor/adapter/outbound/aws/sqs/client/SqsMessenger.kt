package br.com.fiap.video.processor.adapter.outbound.aws.sqs.client

fun interface SqsMessenger {
    fun send(queueName: String, message: String)
}