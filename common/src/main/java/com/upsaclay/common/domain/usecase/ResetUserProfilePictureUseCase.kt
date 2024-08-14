package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.e

class ResetUserProfilePictureUseCase(
    private val userRepository: UserRepository,
    private val updateUserProfilePictureUseCase: UpdateUserProfilePictureUseCase,
    private val imageRepository: ImageRepository,
    private val getDrawableUriUseCase: GetDrawableUriUseCase
) {
    suspend operator fun invoke(userId: Int, currentProfilePictureUrl: String): Result<Unit> {
        return deleteProfilePicture(currentProfilePictureUrl)
            .onFailure { exception ->
                e(
                    exception.message ?: "Error while resetting user profile picture",
                    exception
                )
            }
    }

    private suspend fun deleteProfilePicture(userProfilePictureUrl: String): Result<Unit> {
        val fileName = userProfilePictureUrl.substringAfterLast("/")
        return imageRepository.deleteImage(fileName)
    }
}