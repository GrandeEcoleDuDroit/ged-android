package com.upsaclay.news.domain.usecase

import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(announcement: Announcement) {
        scope.launch {
            try {
                announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
                announcementRepository.upsertLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
            } catch (_: Exception) {
                announcementRepository.upsertLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
            }
        }
    }
}