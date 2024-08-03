package com.upsaclay.common.domain.repository

import android.net.Uri
import java.io.File

interface FileRepository {
    fun createFileFromByteArray(fileName: String, byteArray: ByteArray): File

    fun getFileByteArrayFromUri(uri: Uri): ByteArray
}