package com.upsaclay.app.domain.usecase

import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.news.domain.repository.AnnouncementRepository

class ListenBlockedUserEventsUseCase(
    private val blockedUserRepository: BlockedUserRepository,
    private val announcementRepository: AnnouncementRepository
) {
    suspend fun start() {
        blockedUserRepository.blockUserEvent.collect { event ->
            when (event) {
                is BlockUserEvent.Block -> announcementRepository.deleteLocalUserAnnouncements(event.userId)

                else -> Unit
            }
        }
    }
}