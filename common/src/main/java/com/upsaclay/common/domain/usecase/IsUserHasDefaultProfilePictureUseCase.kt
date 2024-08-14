package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class IsUserHasDefaultProfilePictureUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userRepository.hasDefaultProfilePicture
    }
}