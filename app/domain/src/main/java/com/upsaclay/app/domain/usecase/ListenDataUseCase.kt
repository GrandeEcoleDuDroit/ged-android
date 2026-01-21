package com.upsaclay.app.domain.usecase

import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListenDataUseCase(
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase,
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenBlockedUserEventsUseCase: ListenBlockedUserEventsUseCase
) {
    private var job: Job? = null

    fun start(scope: CoroutineScope, userId: String) {
        job?.cancel()
        job = scope.launch {
            launch { listenRemoteUserUseCase.start(userId) }
            launch { listenRemoteConversationsUseCase.start(this) }
            launch { listenRemoteMessagesUseCase.start(this) }
            launch { listenBlockedUserEventsUseCase.start() }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}