package com.upsaclay.common.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException

class ImageLocalDataSource(private val context: Context) {
    private val contentResolver = context.contentResolver

    fun getFileExtension(uri: String): String =
        contentResolver.getType(uri.toUri())?.split("/")?.last()
            ?: throw FileNotFoundException()

    suspend fun createLocalImage(folderName: String, fileName: String, uri: String): File? = withContext(Dispatchers.IO) {
        val parent = File(context.filesDir, folderName)
        if (!parent.exists()) {
            parent.mkdir()
        }
        createImage(File(parent, fileName), uri.toUri())
    }

    suspend fun createCacheImage(fileName: String, uri: String): File? = withContext(Dispatchers.IO) {
        val type = getFileExtension(uri)
        createImage(File(context.cacheDir, "$fileName.$type"), uri.toUri())
    }

    fun deleteLocalImage(folderName: String, fileName: String) {
        val parent = File(context.filesDir, folderName)
        File(parent, fileName).delete()
    }

    fun deleteCacheImage(fileName: String) {
        File(context.cacheDir, fileName).delete()
    }

    private fun createImage(file: File, uri: Uri): File? {
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