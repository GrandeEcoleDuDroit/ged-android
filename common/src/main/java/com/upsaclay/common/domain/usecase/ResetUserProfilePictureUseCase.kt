package com.upsaclay.common.domain.usecase

import com.upsaclay.common.R
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.e

class ResetUserProfilePictureUseCase(
    private val userRepository: UserRepository,
    private val updateUserProfilePictureUseCase: UpdateUserProfilePictureUseCase,
    private val getDrawableUriUseCase: GetDrawableUriUseCase
) {
    suspend operator fun invoke(userId: Int, currentProfilePictureUrl: String): Result<Unit> {
        val defaultProfilePictureUri = getDrawableUriUseCase(R.drawable.default_profile_picture)!!
        return updateUserProfilePictureUseCase(userId, defaultProfilePictureUri, currentProfilePictureUrl)
            .onSuccess { userRepository.setUserHasDefaultProfilePicture(true) }
            .onFailure { exception ->
                e(
                    exception.message ?: "Error while resetting user profile picture",
                    exception
                )
            }
    }
}