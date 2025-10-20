package com.upsaclay.common.domain.repository

import java.io.File

interface FileRepository {
    suspend fun createFile(fileName: String, uri: String): File
}