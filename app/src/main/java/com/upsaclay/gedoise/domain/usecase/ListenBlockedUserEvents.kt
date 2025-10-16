package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import com.upsaclay.message.domain.usecase.UpdateConversationDeleteTimeUseCase
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ListenBlockedUserEvents(
    private val blockedUserRepository: BlockedUserRepository,
    private val announcementRepository: AnnouncementRepository,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val updateConversationDeleteTimeUseCase: UpdateConversationDeleteTimeUseCase,
    private val scope: CoroutineScope
) {
    fun start() {
        scope.launch {
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
}