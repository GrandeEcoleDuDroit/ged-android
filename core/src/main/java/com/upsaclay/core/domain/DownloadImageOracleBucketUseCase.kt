package com.upsaclay.core.domain

import com.upsaclay.core.data.remote.ImageRepository
import java.io.File

class DownloadImageOracleBucketUseCase(
    private val imageRepository: ImageRepository,
    private val createFileUseCase: CreateFileUseCase
) {
    suspend operator fun invoke(filename: String): File? {
        val byteArray = imageRepository.downloadImage(filename)
        return byteArray?.let { createFileUseCase.createFileFromByteArray(it, filename) }
    }
}