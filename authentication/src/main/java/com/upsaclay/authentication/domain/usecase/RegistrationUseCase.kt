package com.upsaclay.authentication.domain.usecase

import android.net.Uri
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.usecase.CreateFileFromUriUseCase
import com.upsaclay.common.utils.formatProfilePictureFileName
import okio.IOException

class RegistrationUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val createFileFromUriUseCase: CreateFileFromUriUseCase,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(user: User, profilePictureUri: Uri): Result<String> {
        val result = authenticationRepository.register(user)
        return if(result.isSuccess) {
            val userId = result.getOrThrow()
            val imageName = formatProfilePictureFileName(userId)
            val imageFile = createFileFromUriUseCase(imageName, profilePictureUri)
            return imageRepository.uploadImage(imageFile)
        }
        else {
            Result.failure(result.exceptionOrNull()?: IOException())
        }
    }
}