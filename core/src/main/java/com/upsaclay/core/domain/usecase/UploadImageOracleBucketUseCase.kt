package com.upsaclay.core.domain.usecase

import android.net.Uri
import com.upsaclay.core.domain.repository.ImageRepository

class UploadImageOracleBucketUseCase(
    private val imageRepository: ImageRepository,
    private val getFileBytesFromUriUseCase: GetFileBytesFromUriUseCase,
    private val createFileUseCase: CreateFileUseCase
) {
    suspend fun uploadFromUri(fileName: String, uri: Uri){
        val imageByteArray = getFileBytesFromUriUseCase(uri)
        val imageFile = createFileUseCase.createFileFromByteArray(imageByteArray, fileName)
        imageRepository.uploadImage(imageFile)
    }
}