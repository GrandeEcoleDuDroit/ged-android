package com.upsaclay.common.domain.usecase

import android.net.Uri
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository

class UploadImageOracleBucketUseCase(
    private val imageRepository: ImageRepository,
    private val fileRepository: FileRepository,
) {
    suspend fun uploadFromUri(fileName: String, uri: Uri){
        val imageByteArray = fileRepository.getFileByteArrayFromUri(uri)
        val imageFile = fileRepository.createFileFromByteArray(fileName, imageByteArray)
        imageRepository.uploadImage(imageFile)
    }
}