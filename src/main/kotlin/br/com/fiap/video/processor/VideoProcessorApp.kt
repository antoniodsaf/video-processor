package br.com.fiap.video.processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VideoProcessorApp

fun main(args: Array<String>) {
	runApplication<VideoProcessorApp>(*args)
}
