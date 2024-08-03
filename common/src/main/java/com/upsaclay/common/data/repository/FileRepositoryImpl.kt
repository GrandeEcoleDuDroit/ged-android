package com.upsaclay.common.data.repository

import android.content.Context
import android.net.Uri
import com.upsaclay.common.domain.repository.FileRepository
import java.io.ByteArrayOutputStream
import java.io.File

class FileRepositoryImpl(
    private val context: Context
): FileRepository {
    override fun createFileFromByteArray(fileName: String, byteArray: ByteArray): File {
        val file = File(context.cacheDir, fileName)
        val outPutStream = file.outputStream()
        outPutStream.write(byteArray)
        outPutStream.close()
        return file
    }

    override fun getFileByteArrayFromUri(uri: Uri): ByteArray {
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
}