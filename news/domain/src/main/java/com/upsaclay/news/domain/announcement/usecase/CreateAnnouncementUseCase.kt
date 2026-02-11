package com.upsaclay.news.domain.announcement.usecase

import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import com.upsaclay.news.domain.announcement.AnnouncementJobQueue
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val announcementJobQueue: AnnouncementJobQueue,
    private val scope: CoroutineScope
) {
    suspend fun execute(announcement: Announcement) {
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