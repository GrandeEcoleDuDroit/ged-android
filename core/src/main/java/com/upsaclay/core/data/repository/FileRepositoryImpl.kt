package com.upsaclay.core.data.repository

import android.content.Context
import com.upsaclay.core.domain.repository.FileRepository
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
}