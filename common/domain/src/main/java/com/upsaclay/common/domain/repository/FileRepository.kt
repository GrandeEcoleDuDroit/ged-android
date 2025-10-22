package com.upsaclay.common.domain.repository

import java.io.File

interface FileRepository {
    suspend fun createCacheFile(fileName: String, uri: String): File?

    suspend fun createLocalFile(fileName: String, uri: String): File?
}