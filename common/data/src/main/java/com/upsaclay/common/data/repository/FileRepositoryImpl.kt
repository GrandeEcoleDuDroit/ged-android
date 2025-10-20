package com.upsaclay.common.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.upsaclay.common.domain.entity.InvalidFormatFileException
import com.upsaclay.common.domain.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

internal class FileRepositoryImpl(private val context: Context) : FileRepository {
    override suspend fun createFile(fileName: String, uri: String): File = withContext(Dispatchers.IO) {
        val parsedUri = uri.toUri()
        val extension = getType(parsedUri) ?: throw InvalidFormatFileException()
        val bytes = readBytes(parsedUri)
        return@withContext writeNewFile("$fileName.$extension", bytes)
    }

    private suspend fun writeNewFile(fileName: String, bytes: ByteArray): File = withContext(Dispatchers.IO) {
        val file = File(context.cacheDir, fileName)
        val outPutStream = file.outputStream()
        outPutStream.write(bytes)
        outPutStream.close()
        file
    }

    private fun readBytes(uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int

        inputStream?.let {
            while (inputStream.read(buffer).also { length = it } > 0) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
            inputStream.close()
        }

        return byteArrayOutputStream.toByteArray()
    }

    private fun getType(uri: Uri): String? =
        context.contentResolver.getType(uri)?.split("/")?.last()
}
