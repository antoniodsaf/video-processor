package br.com.fiap.video

import br.com.fiap.video.processor.util.convertMultiPartToFile
import br.com.fiap.video.processor.util.generateRandomFileKey
import br.com.fiap.video.processor.util.generateRandomFileName
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.io.File
import java.nio.file.Files

class FileUtilTest {

    @Test
    fun `test convertMultiPartToFile`() {
        val content = "Hello, World!".toByteArray()
        val multipartFile = MockMultipartFile("file", "hello.txt", "text/plain", content)

        val file: File = convertMultiPartToFile(multipartFile)

        assertTrue(file.exists())
        assertTrue(file.name.contains("hello.txt"))
        assertArrayEquals(content, Files.readAllBytes(file.toPath()))

        // Clean up
        file.delete()
    }

    @Test
    fun `test generateRandomFileKey`() {
        val fileKey = generateRandomFileKey()
        assertNotNull(fileKey)
        assertEquals(36, fileKey.length)
    }

    @Test
    fun `test generateRandomFileName`() {
        val originalFilename = "example.txt"
        val randomFileName = generateRandomFileName(originalFilename)

        assertNotNull(randomFileName)
        assertTrue(randomFileName.endsWith(".txt"))
        assertEquals(40, randomFileName.length) // 36 (UUID) + 1 (dot) + 3 (extension)
    }
}