package com.upsaclay.common.domain.repository

import java.io.File

interface ImageRepository {
    suspend fun downloadImageWithServer(fileName: String): ByteArray?

    suspend fun uploadImage(file: File): Result<String>

    suspend fun deleteImage(fileName: String): Result<Unit>
}