package com.upsaclay.common.domain.repository

import java.io.File

interface ImageRepository {
    suspend fun uploadImage(file: File): Result<Unit>

    suspend fun deleteImage(fileName: String): Result<Unit>
}