package com.upsaclay.authentication.domain.usecase

import android.net.Uri
import com.upsaclay.common.domain.usecase.UpdateUserProfilePictureUseCase
import java.io.IOException

class RegistrationUseCase(
    private val userRepository: com.upsaclay.common.domain.repository.UserRepository,
    private val updateUserProfilePictureUseCase: UpdateUserProfilePictureUseCase
) {
    suspend operator fun invoke(user: com.upsaclay.common.domain.model.User, profilePictureUri: Uri?): Result<Int> {
        return userRepository.createUser(user)?.let { userId ->
            profilePictureUri?.let {
                updateUserProfilePictureUseCase(userId, profilePictureUri, null)
                Result.success(userId)
            }
        } ?: Result.failure(IOException())
    }
}