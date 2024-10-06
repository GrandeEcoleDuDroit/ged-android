package com.upsaclay.common.domain.repository

import android.net.Uri
import java.io.File

interface FileRepository {
    suspend fun createFileFromUri(fileName: String, uri: Uri): File

    suspend fun createFileFromByteArray(fileName: String, byteArray: ByteArray): File

    fun getFileType(uri: Uri): String?
}