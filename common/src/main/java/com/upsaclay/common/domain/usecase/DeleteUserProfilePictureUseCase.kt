package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.e

class DeleteUserProfilePictureUseCase(
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(userId: Int, currentProfilePictureUrl: String): Result<Unit> {
        return userRepository.deleteProfilePictureUrl(userId)
            .onSuccess { deleteProfilePictureImage(currentProfilePictureUrl) }
            .onFailure { exception ->
                e(
                    exception.message ?: "Error during the deletion of the user profile picture",
                    exception
                )
            }
    }

    private suspend fun deleteProfilePictureImage(userProfilePictureUrl: String): Result<Unit> {
        val fileName = userProfilePictureUrl.substringAfterLast("/")
        return imageRepository.deleteImage(fileName)
    }
}