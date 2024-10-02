package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository

class DeleteUserProfilePictureUseCase(
    private val userRepository: com.upsaclay.common.domain.repository.UserRepository,
    private val imageRepository: com.upsaclay.common.domain.repository.ImageRepository,
) {
    suspend operator fun invoke(userId: Int, currentProfilePictureUrl: String): Result<Unit> {
        val urlDeletionResult = userRepository.deleteProfilePictureUrl(userId)

        return if (urlDeletionResult.isSuccess) {
            val fileName = currentProfilePictureUrl.substringAfterLast("/")
            imageRepository.deleteImage(fileName)
        } else {
            urlDeletionResult
        }
    }
}