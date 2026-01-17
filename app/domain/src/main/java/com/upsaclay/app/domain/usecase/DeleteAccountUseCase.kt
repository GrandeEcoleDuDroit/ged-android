package com.upsaclay.app.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.news.domain.repository.AnnouncementRepository

class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val announcementRepository: AnnouncementRepository,
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
            state = User.UserState.DELETED
        )

        //TODO Delete user
        userRepository.deleteLocalUser()
    }
}