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
            val fileName = imageRepository.uploadImage(profilePictureFileName(user.id), profilePictureUri)
            userRepository.updateProfilePictureFileName(user.id, fileName)
            user.profilePictureUrl?.let { imageRepository.deleteRemoteImage(it) }
        }
    }

    private fun profilePictureFileName(userId: String) = "${userId}-profile-picture-${System.currentTimeMillis()}"
}