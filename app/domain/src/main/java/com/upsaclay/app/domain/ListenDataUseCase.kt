package com.upsaclay.app.domain

import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase

class ListenDataUseCase(
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase,
    private val listenBlockedUserEvents: ListenBlockedUserEvents
) {
    suspend fun start() {
        listenRemoteConversationsUseCase.start()
        listenRemoteUserUseCase.start()
        listenBlockedUserEvents.start()
    }

    suspend fun stop() {
        listenRemoteMessagesUseCase.stopAll()
    }
}