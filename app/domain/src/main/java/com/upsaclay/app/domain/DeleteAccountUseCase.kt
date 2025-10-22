package com.upsaclay.app.domain

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.news.domain.repository.AnnouncementRepository

class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val announcementRepository: AnnouncementRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(user: User, password: String) {
        authenticationRepository.loginWithEmailAndPassword(user.email, password)
        announcementRepository.deleteAnnouncements(user.id)
        deleteUser(user)
        authenticationRepository.deleteAuthUser()
    }

    private suspend fun deleteUser(user: User) {
        val deletedUser = user.copy(
            email = user.id + "@deleted.com",
            profilePictureUrl = null,
            isDeleted = true
        )

        userRepository.updateRemoteUser(deletedUser)
        user.profilePictureUrl?.let { imageRepository.deleteImage(it) }
        userRepository.deleteLocalUser()
    }
}