package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.withTimeout

class UpdateProfilePictureUseCase(
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User, profilePictureUri: String) {
        withTimeout(15000) {
            imageRepository.createCacheImage(formatProfilePictureFileName(user.id), profilePictureUri)?.let { file ->
                imageRepository.uploadImage(file)
                userRepository.updateProfilePictureFileName(user.id, file.name)
                imageRepository.deleteCacheImage(file.name)
            }

            user.profilePictureUrl?.let { url ->
                imageRepository.deleteRemoteImage(url)
            }

        }
    }

    private fun formatProfilePictureFileName(userId: String) = "${userId}-profile-picture-${System.currentTimeMillis()}"
}