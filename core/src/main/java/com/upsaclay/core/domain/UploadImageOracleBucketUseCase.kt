package com.upsaclay.core.domain

import android.net.Uri
import com.upsaclay.core.data.remote.ImageRepository

class UploadImageOracleBucketUseCase(
    private val imageRepository: ImageRepository,
    private val getBytesFileFromUriUseCase: GetBytesFileFromUriUseCase,
    private val createFileUseCase: CreateFileUseCase
) {
    suspend fun uploadFromUri(fileName: String, uri: Uri){
        val imageByteArray = getBytesFileFromUriUseCase(uri)
        imageRepository.uploadImage(createFileUseCase.createFileFromByteArray(imageByteArray, fileName))
    }
}