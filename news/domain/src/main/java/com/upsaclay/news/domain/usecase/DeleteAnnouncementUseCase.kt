package com.upsaclay.news.domain.usecase

import com.upsaclay.news.domain.AnnouncementJobQueue
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository

class DeleteAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val announcementJobQueue: AnnouncementJobQueue
) {
    suspend operator fun invoke(announcement: Announcement) {
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