package com.upsaclay.news.domain.usecase

import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateAnnouncementUseCase(private val announcementRepository: AnnouncementRepository) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(announcement: Announcement) {
        GlobalScope.launch {
            try {
                announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
                announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
            } catch (_: Exception) {
                announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
            }
        }
    }
}