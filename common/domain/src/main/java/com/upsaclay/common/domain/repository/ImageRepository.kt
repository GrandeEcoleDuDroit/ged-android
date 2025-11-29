package com.upsaclay.common.domain.repository

import java.io.File
import java.io.InputStream

interface ImageRepository {
    suspend fun getRemoteImage(fileName: String): InputStream?

    fun getFileExtension(uri: String): String

    suspend fun createLocalImage(folderName: String, fileName: String, uri: String): File?

    suspend fun createCacheImage(fileName: String, uri: String): File?

    suspend fun uploadImage(file: File)

    suspend fun deleteRemoteImage(url: String)

    suspend fun deleteLocalImage(folderName: String, fileName: String)

    suspend fun deleteCacheImage(fileName: String)
}