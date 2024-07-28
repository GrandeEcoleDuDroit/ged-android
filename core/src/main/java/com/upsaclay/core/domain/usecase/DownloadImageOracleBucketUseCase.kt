package com.upsaclay.core.domain.usecase

import com.upsaclay.core.domain.repository.FileRepository
import com.upsaclay.core.domain.repository.ImageRepository
import java.io.File

class DownloadImageOracleBucketUseCase(
    private val imageRepository: ImageRepository,
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(filename: String): File? {
        val byteArray = imageRepository.downloadImage(filename)
        return byteArray?.let { fileRepository.createFileFromByteArray(filename, it) }
    }
}