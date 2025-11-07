package com.upsaclay.common.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.upsaclay.common.data.UrlUtils
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.domain.entity.InvalidFormatFileException
import com.upsaclay.common.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

internal class ImageRepositoryImpl(
    private val context: Context,
    private val imageRemoteDataSource: ImageRemoteDataSource
): ImageRepository {
    private val contentResolver = context.contentResolver

    override suspend fun getImage(fileName: String): InputStream? =
        imageRemoteDataSource.getImage(fileName)

    override suspend fun createLocalImage(fileName: String, uri: String): File? = withContext(Dispatchers.IO) {
        val type = getType(uri)
        val file = File(context.filesDir, "$fileName.$type")
        return@withContext writeFile(file, uri.toUri())
    }

    override suspend fun uploadImage(fileName: String, uri: String): String = withContext(Dispatchers.IO) {
        val type = getType(uri)
        val file = File(context.cacheDir, "$fileName.$type")
        writeFile(file, uri.toUri())?.let {
            imageRemoteDataSource.uploadImage(it)
        }
        file.name
    }

    override suspend fun deleteImage(url: String) {
        UrlUtils.extractFileName(url)?.let {
            imageRemoteDataSource.deleteImage(it)
        }
    }

    private fun getType(uri: String): String {
        return contentResolver.getType(uri.toUri())?.split("/")?.last()
            ?: throw InvalidFormatFileException()
    }

    private fun writeFile(file: File, uri: Uri): File? {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val compressFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap.compress(compressFormat, 70, file.outputStream())
        return file
    }
}