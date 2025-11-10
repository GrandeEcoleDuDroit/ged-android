package com.upsaclay.common.domain.repository

import java.io.File
import java.io.InputStream

interface ImageRepository {
    suspend fun getImage(fileName: String): InputStream?

    suspend fun uploadImage(fileName: String, uri: String): String

    suspend fun createLocalImage(fileName: String, uri: String): File?

    suspend fun deleteRemoteImage(url: String)

    suspend fun deleteLocalImage(fileName: String)
}