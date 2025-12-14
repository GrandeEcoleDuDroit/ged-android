package com.upsaclay.common.domain.repository

import java.io.File
import java.io.InputStream

interface ImageRepository {
    suspend fun getRemoteImage(fileName: String): InputStream?

    fun getFileExtension(uri: String): String

    suspend fun createLocalImage(imagePath: String, uri: String): File?

    suspend fun createCacheImage(fileName: String, uri: String): File?

    suspend fun uploadImage(file: File, imagePath: String)

    suspend fun deleteRemoteImage(imagePath: String)

    suspend fun deleteLocalImage(imagePath: String)

    suspend fun deleteCacheImage(imagePath: String)
}