package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.FileInformation
import java.io.File
import java.io.InputStream

interface ImageRepository {
    suspend fun getRemoteImage(fileName: String): InputStream?

    fun getFileInformation(uri: String): FileInformation

    fun getFileExtension(uri: String): String

    suspend fun createLocalImage(imagePath: String, uri: String): File?

    suspend fun createCacheImage(fileName: String, uri: String): File?

    suspend fun deleteLocalImage(imagePath: String)

    suspend fun deleteCacheImage(imagePath: String)
}