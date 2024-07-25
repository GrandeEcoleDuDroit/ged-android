package com.upsaclay.core.domain

import android.content.Context
import android.net.Uri
import java.io.ByteArrayOutputStream

class GetBytesFileFromUriUseCase(
    private val context: Context,
) {
    operator fun invoke(uri: Uri): ByteArray {
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