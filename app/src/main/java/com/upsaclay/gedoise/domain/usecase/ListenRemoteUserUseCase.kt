package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class ListenRemoteUserUseCase(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val scope: CoroutineScope
) {
    internal var job: Job? = null

    fun start() {
        job = scope.launch {
            userRepository.user.take(1).collect { user ->
                userRepository.getUserFlow(user.id)
                    .filter { it != user }
                    .collect { user ->
                        user?.let {
                            userRepository.storeUser(it)
                        } ?: authenticationRepository.logout()
                    }
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}