package com.upsaclay.common.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.core.net.toUri
import com.upsaclay.common.domain.entity.FileInformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException

class ImageLocalDataSource(private val context: Context) {
    private val contentResolver = context.contentResolver

    fun getFileInformation(uri: String): FileInformation {
        return contentResolver.query(uri.toUri(), null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()

            FileInformation(
                displayName = cursor.getString(nameIndex),
                size = cursor.getLong(sizeIndex)
            )
        } ?: FileInformation()
    }

    fun getFileExtension(uri: String): String =
        contentResolver.getType(uri.toUri())?.split("/")?.last()
            ?: throw FileNotFoundException()

    suspend fun createLocalImage(imagePath: String, uri: String): File? = withContext(Dispatchers.IO) {
        val image = File(context.filesDir, imagePath)
        val parent = File(image.path.substringBeforeLast("/"))
        parent.mkdirs()
        writeImage(image, uri.toUri())

    }

    suspend fun createCacheImage(imagePath: String, uri: String): File? = withContext(Dispatchers.IO) {
        writeImage(File(context.cacheDir, imagePath), uri.toUri())
    }

    fun deleteLocalImage(imagePath: String) {
        File(context.filesDir, imagePath).delete()
    }

    fun deleteCacheImage(imagePath: String) {
        File(context.cacheDir, imagePath).delete()
    }

    private fun writeImage(file: File, uri: Uri): File? {
        file.parentFile?.mkdirs()

        val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }

        contentResolver.openInputStream(uri)?.use { input ->
            val bitmap = BitmapFactory.decodeStream(input) ?: return null
            file.outputStream().use { output ->
                bitmap.compress(format, 70, output)
            }
        } ?: return null

        return file
    }
}