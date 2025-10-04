package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase

class ListenDataUseCase(
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase,
    private val listenBlockedUserEvents: ListenBlockedUserEvents
) {
    fun start() {
        listenRemoteConversationsUseCase.start()
        listenRemoteUserUseCase.start()
        listenBlockedUserEvents.start()
    }

    fun stop() {
        listenRemoteConversationsUseCase.stop()
        listenRemoteMessagesUseCase.stopAll()
        listenRemoteUserUseCase.stop()
    }
}