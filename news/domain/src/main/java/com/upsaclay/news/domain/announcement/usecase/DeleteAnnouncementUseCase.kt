package com.upsaclay.news.domain.announcement.usecase

import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import com.upsaclay.news.domain.announcement.AnnouncementJobQueue
import com.upsaclay.news.domain.announcement.AnnouncementRepository

class DeleteAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val announcementJobQueue: AnnouncementJobQueue
) {
    suspend fun execute(announcement: Announcement) {
        when (announcement.state) {
            AnnouncementState.PUBLISHED -> announcementRepository.deleteAnnouncement(announcement)

            AnnouncementState.PUBLISHING -> {
                announcementJobQueue.cancelAndRemoveJob(announcement.id)
                announcementRepository.deleteLocalAnnouncement(announcement)
            }

            AnnouncementState.ERROR, AnnouncementState.DRAFT -> announcementRepository.deleteLocalAnnouncement(announcement)
        }
    }
}