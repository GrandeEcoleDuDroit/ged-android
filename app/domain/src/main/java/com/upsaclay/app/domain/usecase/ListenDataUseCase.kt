package com.upsaclay.app.domain.usecase

import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListenDataUseCase(
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase,
    private val listenBlockedUserEventsUseCase: ListenBlockedUserEventsUseCase,
    private val scope: CoroutineScope
) {
    private var job: Job? = null

    fun start() {
        job?.cancel()
        job = scope.launch {
            launch { listenRemoteUserUseCase.start() }
            launch { listenRemoteConversationsUseCase.start() }
            launch { listenBlockedUserEventsUseCase.start() }
        }
    }

    fun stop() {
        scope.launch {
            listenRemoteMessagesUseCase.stopAll()
        }
        job?.cancel()
        job = null
    }
}