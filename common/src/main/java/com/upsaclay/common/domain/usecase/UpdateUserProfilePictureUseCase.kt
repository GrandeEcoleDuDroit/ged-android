package com.upsaclay.common.domain.usecase

import android.net.Uri
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.formatProfilePictureUrl

class UpdateUserProfilePictureUseCase(
    private val fileRepository: FileRepository,
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        userId: Int,
        profilePictureUri: Uri,
        currentProfilePictureUrl: String
    ): Result<Unit> {
        val currentTime = System.currentTimeMillis()
        val fileName = "$userId-profile-picture$currentTime"
        val profilePictureFile = fileRepository.createFileFromUri(fileName, profilePictureUri)
        val uploadImageResult = imageRepository.uploadImage(profilePictureFile)

        return if(uploadImageResult.isSuccess) {
            val profilePictureUrl = formatProfilePictureUrl(fileName, profilePictureFile.extension)
            userRepository.updateProfilePictureUrl(userId, profilePictureUrl)
                .onSuccess { deleteProfilePicture(currentProfilePictureUrl) }
        }
        else {
            Result.failure(uploadImageResult.exceptionOrNull()!!)
        }
    }

    private suspend fun deleteProfilePicture(userProfilePictureUrl: String): Result<Unit> {
        val fileName = userProfilePictureUrl.substringAfterLast("/")
        return userRepository.deleteProfilePicture(fileName)
    }
}