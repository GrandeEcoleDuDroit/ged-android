package com.upsaclay.news.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository

class SynchronizeAnnouncementsUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val blockedUserRepository: BlockedUserRepository
) {
    suspend operator fun invoke() {
        val announcements = announcementRepository.currentAnnouncements
        val remoteAnnouncements = announcementRepository.getRemoteAnnouncements()
        val blockedUserIds = blockedUserRepository.getLocalBlockedUserIds()

        val announcementsToDelete = announcements.filter {
            (it.state == AnnouncementState.PUBLISHED && it !in remoteAnnouncements) ||
                    it.author.id in blockedUserIds
        }
        val announcementsToUpsert = remoteAnnouncements.filter {
            it !in announcements && it.author.id !in blockedUserIds
        }

        announcementsToDelete.forEach { announcementRepository.deleteLocalAnnouncement(it) }
        announcementsToUpsert.forEach { announcementRepository.upsertLocalAnnouncement(it) }
    }
}