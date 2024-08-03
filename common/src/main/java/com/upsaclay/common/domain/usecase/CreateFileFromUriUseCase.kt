package com.upsaclay.common.domain.usecase

import android.net.Uri
import com.upsaclay.common.domain.repository.FileRepository
import java.io.File

class CreateFileFromUriUseCase(
    private val fileRepository: FileRepository
) {
    operator fun invoke(fileName: String, uri: Uri): File {
        val imageByteArray = fileRepository.getFileByteArrayFromUri(uri)
        return fileRepository.createFileFromByteArray(fileName, imageByteArray)
    }
}