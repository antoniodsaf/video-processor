package br.com.fiap.video.processor.util

import org.apache.commons.io.FilenameUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


/**
 * Converts a MultipartFile to a temporary File.
 *
 * @param multipartFile the MultipartFile to be converted
 * @return the converted File
 * @throws IOException if any I/O error occurs during file creation or writing
 */
@Throws(IOException::class)
fun convertMultiPartToFile(multipartFile: MultipartFile): File {
    // Create a temporary file
    val tempFile: Path = Files.createTempFile(multipartFile.originalFilename, null)

    FileOutputStream(tempFile.toFile()).use { fos ->
        fos.write(multipartFile.bytes)
    }
    // Return the temporary file
    return tempFile.toFile()
}

const val EXTENSION_ZIP = "zip"

/**
 * Generates a random, unique file key using UUID.
 *
 * @return a unique random file key in UUID string format.
 */
fun generateRandomFileKey(): String {
    return UUID.randomUUID().toString()
}

/**
 * Generates a random, unique file name using UUID and the original file's extension.
 *
 * @param originalFilename the original filename from which to extract the extension.
 * @return a unique random file name with the original extension.
 */
fun generateRandomFileName(originalFilename: String?): String {
    // Extract the file extension from the original filename
    val extension: String = FilenameUtils.getExtension(originalFilename)
    // Generate a random UUID for the file name, and return it with the original extension
    return UUID.randomUUID().toString() + "." + extension
}