package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository

class DeleteUserProfilePictureUseCase(
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(userId: Int, currentProfilePictureUrl: String): Result<Unit> {
        val urlDeletionResult = userRepository.deleteProfilePictureUrl(userId)

        return if (urlDeletionResult.isSuccess) {
            deleteProfilePictureImage(currentProfilePictureUrl)
        } else {
            urlDeletionResult
        }
    }

    private suspend fun deleteProfilePictureImage(userProfilePictureUrl: String): Result<Unit> {
        val fileName = userProfilePictureUrl.substringAfterLast("/")
        return imageRepository.deleteImage(fileName)
    }
}