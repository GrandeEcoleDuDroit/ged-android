package com.upsaclay.app.domain.usecase

import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ListenDataUseCase(
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenBlockedUserEventsUseCase: ListenBlockedUserEventsUseCase
) {
    private var job: Job? = null

    fun start(scope: CoroutineScope, handler: CoroutineExceptionHandler) {
        job?.cancel()
        job = scope.launch {
            supervisorScope {
                launch(handler) { listenRemoteConversationsUseCase.start(this) }
                launch(handler) { listenRemoteMessagesUseCase.start(this) }
                launch(handler) { listenBlockedUserEventsUseCase.start() }
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}