package com.upsaclay.news.domain.announcement.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import com.upsaclay.news.domain.announcement.AnnouncementRepository

class FetchAnnouncementsUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val blockedUserRepository: BlockedUserRepository
) {
    suspend fun execute() {
        val localAnnouncements = announcementRepository.currentAnnouncements
        val remoteAnnouncements = announcementRepository.getRemoteAnnouncements()
        val localBlockedUsers = blockedUserRepository.getLocalBlockedUsers()

        val announcementsToDelete = localAnnouncements.filter {
            (it.state == AnnouncementState.PUBLISHED && it !in remoteAnnouncements) ||
                    localBlockedUsers.containsKey(it.author.id)
        }
        val announcementsToUpsert = remoteAnnouncements.filter {
            it !in localAnnouncements && !localBlockedUsers.containsKey(it.author.id)
        }

        announcementsToDelete.forEach { announcementRepository.deleteLocalAnnouncement(it) }
        announcementsToUpsert.forEach { announcementRepository.upsertLocalAnnouncement(it) }
    }
}