package com.upsaclay.authentication.domain.usecase

import android.net.Uri
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.UpdateUserProfilePictureUseCase
import com.upsaclay.common.utils.e
import com.upsaclay.common.utils.i
import okio.IOException

class RegistrationUseCase(
    private val userRepository: UserRepository,
    private val updateUserProfilePictureUseCase: UpdateUserProfilePictureUseCase
) {
    suspend operator fun invoke(user: User, profilePictureUri: Uri): Result<Int> {
        return userRepository.createUser(user)
            .onSuccess { userId ->
                i("Registration successful")
                val profilePictureResult = updateUserProfilePictureUseCase(userId, profilePictureUri, null)
                if (profilePictureResult.isFailure) {
                    e("Error during profile picture update", profilePictureResult.exceptionOrNull() ?:
                        IOException("Error update profile picture")
                    )
                }
            }
            .onFailure { exception ->
                e("Error during registration", exception)
            }
    }
}