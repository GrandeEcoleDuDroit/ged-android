package com.upsaclay.core.domain.repository

import java.io.File

interface FileRepository {
    fun createFileFromByteArray(fileName: String, byteArray: ByteArray): File
}