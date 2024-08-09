package com.upsaclay.common.domain.usecase

import android.net.Uri
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository

class UploadImageToOracleBucketUseCase(
    private val imageRepository: ImageRepository,
    private val fileRepository: FileRepository
) {
    suspend fun uploadFromUri(fileName: String, uri: Uri){
        imageRepository.uploadImage(fileRepository.createFileFromUri(fileName, uri))
    }
}