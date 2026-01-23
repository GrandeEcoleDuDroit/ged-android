package com.upsaclay.common.domain.repository

import java.io.File

interface FileRepository {
    suspend fun getFile(path: String): File?
}