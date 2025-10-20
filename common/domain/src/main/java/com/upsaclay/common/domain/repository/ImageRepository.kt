package com.upsaclay.common.domain.repository

import java.io.File
import java.io.InputStream

interface ImageRepository {
    suspend fun getImage(fileName: String): InputStream?

    suspend fun uploadImage(file: File)

    suspend fun deleteImage(fileName: String)
}