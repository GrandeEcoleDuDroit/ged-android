package com.upsaclay.common.domain.usecase

import android.net.Uri
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.formatProfilePictureUrl

class UpdateUserProfilePictureUseCase(
    private val fileRepository: FileRepository,
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int, profilePictureUri: Uri): Result<Unit> {
        val fileName = "$userId-profile-picture"
        val profilePictureFile = fileRepository.createFileFromUri(fileName, profilePictureUri)
        val uploadImageResult = imageRepository.uploadImage(profilePictureFile)
        val profilePictureUrl = formatProfilePictureUrl(userId, profilePictureFile.extension)

        return if(uploadImageResult.isSuccess) {
            userRepository.updateProfilePictureUrl(userId, profilePictureUrl)
        }
        else {
            Result.failure(uploadImageResult.exceptionOrNull()!!)
        }
    }
}