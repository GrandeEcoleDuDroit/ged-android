package com.upsaclay.core.domain.repository

import java.io.File

interface ImageRepository {
    suspend fun downloadImage(fileName: String): ByteArray?
    suspend fun uploadImage(file: File): Result<String>
}