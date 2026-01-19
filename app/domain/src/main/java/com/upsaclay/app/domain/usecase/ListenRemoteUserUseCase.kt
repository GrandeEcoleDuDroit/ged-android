package com.upsaclay.app.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull

class ListenRemoteUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun start(userId: String) {
        userRepository.getUserFlow(userId)
            .filterNotNull()
            .filter { it != userRepository.currentUser }
            .collect {
                userRepository.storeUser(it)
            }
    }
}