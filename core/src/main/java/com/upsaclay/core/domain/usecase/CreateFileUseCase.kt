package com.upsaclay.core.domain.usecase

import android.content.Context
import java.io.File

class CreateFileUseCase(
    private val context: Context
) {
    fun createFileFromByteArray(byteArray: ByteArray, fileName: String): File {
        val file = File(context.cacheDir, fileName)
        val outPutStream = file.outputStream()
        outPutStream.write(byteArray)
        outPutStream.close()
        return file
    }
}