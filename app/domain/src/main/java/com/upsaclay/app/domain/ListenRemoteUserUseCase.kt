package com.upsaclay.app.domain

import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take

class ListenRemoteUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun start() {
        userRepository.user.take(1).collect { currentUser ->
            userRepository.getUserFlow(currentUser.id)
                .filterNotNull()
                .filter { it != currentUser }
                .collect {
                    userRepository.storeUser(it)
                }
        }
    }
}