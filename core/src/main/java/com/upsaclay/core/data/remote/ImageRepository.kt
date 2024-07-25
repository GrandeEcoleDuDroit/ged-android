package com.upsaclay.core.data.remote

import java.io.File

interface ImageRepository {
    suspend fun downloadImage(fileName: String): ByteArray?
    suspend fun uploadImage(file: File)
}