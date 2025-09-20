package com.upsaclay.news.domain.usecase

import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ListenBlockUserEvents(
    private val blockedUserRepository: BlockedUserRepository,
    private val announcementRepository: AnnouncementRepository,
    private val scope: CoroutineScope
) {
    fun start() {
        scope.launch {
            blockedUserRepository.blockUserEvent.collect { event ->
                if (event is BlockUserEvent.Block) {
                    announcementRepository.deleteLocalUserAnnouncements(event.userId)
                }
            }
        }
    }
}