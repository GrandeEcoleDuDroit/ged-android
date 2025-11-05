package com.upsaclay.common.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.upsaclay.common.domain.entity.InvalidFormatFileException
import com.upsaclay.common.domain.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class FileRepositoryImpl(private val context: Context): FileRepository {
    private val contentResolver = context.contentResolver

    override suspend fun createCacheFile(fileName: String, uri: String): File? = withContext(Dispatchers.IO) {
        val name = getFileName(uri)
        val file = File(context.cacheDir, name)
        return@withContext writeFile(file, uri.toUri())
    }

    override suspend fun createLocalFile(fileName: String, uri: String): File? = withContext(Dispatchers.IO) {
        val name = getFileName(uri)
        val file = File(context.filesDir, name)
        return@withContext writeFile(file, uri.toUri())
    }

    override suspend fun getFile(path: String): File? {
        return withContext(Dispatchers.IO) {
            val file = File(path)
            if (file.exists()) file else null
        }
    }

    private fun getFileName(uri: String): String {
        return contentResolver.getType(uri.toUri())?.split("/")?.last()
            ?: throw InvalidFormatFileException()
    }

    private fun writeFile(file: File, uri: Uri): File? {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
