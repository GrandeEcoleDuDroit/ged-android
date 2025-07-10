package com.upsaclay.news.domain.usecase

import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

class CreateAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val scope: CoroutineScope
) {
    @OptIn(InternalSerializationApi::class)
    operator fun invoke(announcement: Announcement) {
        scope.launch {
            try {
                announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
                announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
            } catch (_: Exception) {
                 announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
            }
        }
    }
}