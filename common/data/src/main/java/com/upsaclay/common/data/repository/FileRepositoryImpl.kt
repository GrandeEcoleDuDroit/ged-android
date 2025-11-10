package com.upsaclay.common.data.repository

import com.upsaclay.common.domain.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class FileRepositoryImpl: FileRepository {
    override suspend fun getFile(path: String): File? {
        return withContext(Dispatchers.IO) {
            val file = File(path)
            if (file.exists()) file else null
        }
    }
}
