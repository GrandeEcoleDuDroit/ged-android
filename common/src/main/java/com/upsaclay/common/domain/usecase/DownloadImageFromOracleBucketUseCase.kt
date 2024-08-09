package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import java.io.File

class DownloadImageFromOracleBucketUseCase(
    private val imageRepository: ImageRepository,
    private val fileRepository: FileRepository
) {
    suspend fun downloadWithServer(filename: String): File? {
        val byteArray = imageRepository.downloadImageWithServer(filename)
        return byteArray?.let { fileRepository.createFileFromByteArray(filename, it) }
    }

    suspend fun downloadWithUrl(filename: String): File? {
        val byteArray = imageRepository.downloadImageWithServer(filename)
        return byteArray?.let { fileRepository.createFileFromByteArray(filename, it) }
    }
}