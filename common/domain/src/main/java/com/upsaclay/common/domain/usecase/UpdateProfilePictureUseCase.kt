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
    suspend operator fun invoke(user: User, profilePictureUri: String) {
        withTimeout(15000) {
            val fileName = UserUtils.ProfilePicture.generateFileName(user.id)
            val imagePath = UserUtils.ProfilePicture.makeRelativePath(fileName)

            imageRepository.createCacheImage(fileName, profilePictureUri)?.let { file ->
                imageRepository.uploadImage(file, imagePath)
                userRepository.updateProfilePicture(user, file, file.name)
                imageRepository.deleteCacheImage(file.name)
            }

            UserUtils.ProfilePicture.getPath(user.profilePictureUrl)?.let {
                imageRepository.deleteRemoteImage(it)
            }
        }
    }
}