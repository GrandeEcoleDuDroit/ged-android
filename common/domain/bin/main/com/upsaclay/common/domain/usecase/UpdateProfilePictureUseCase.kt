package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.UserUtils
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.withTimeout

class UpdateProfilePictureUseCase(
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(user: User, profilePictureUri: String) {
        withTimeout(15000) {
            val fileName = UserUtils.ProfilePicture.generateFileName(user.id)
            imageRepository.createCacheImage(fileName, profilePictureUri)?.let { file ->
                userRepository.updateProfilePicture(user, file, file.name)
                imageRepository.deleteCacheImage(file.name)
            }
        }
    }
}