package com.upsaclay.app.domain

import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import com.upsaclay.message.domain.usecase.UpdateConversationDeleteTimeUseCase
import com.upsaclay.news.domain.repository.AnnouncementRepository

class ListenBlockedUserEvents(
    private val blockedUserRepository: BlockedUserRepository,
    private val announcementRepository: AnnouncementRepository,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val updateConversationDeleteTimeUseCase: UpdateConversationDeleteTimeUseCase
) {
    suspend fun start() {
        blockedUserRepository.blockUserEvent.collect { event ->
            when (event) {
                is BlockUserEvent.Block -> {
                    listenRemoteMessagesUseCase.stop(event.userId)
                    announcementRepository.deleteLocalAnnouncements(event.userId)
                }

                is BlockUserEvent.Unblock -> {
                    updateConversationDeleteTimeUseCase.execute(event.userId, event.date)
                }
            }
        }
    }
}