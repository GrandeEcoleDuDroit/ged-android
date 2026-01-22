package com.upsaclay.news.domain.usecase

import com.upsaclay.news.domain.AnnouncementJobQueue
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RecreateAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val announcementJobQueue: AnnouncementJobQueue,
    private val scope: CoroutineScope
) {
    suspend fun execute(announcement: Announcement) {
        if (announcement.state == AnnouncementState.ERROR) {
            val job = scope.launch {
                try {
                    announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
                    announcementRepository.upsertLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
                    announcementJobQueue.cancelAndRemoveJob(announcement.id)
                } catch (_: Exception) {
                    announcementRepository.upsertLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
                    announcementJobQueue.cancelAndRemoveJob(announcement.id)
                }
            }

            announcementJobQueue.addJob(job, announcement.id)
        }
    }
}