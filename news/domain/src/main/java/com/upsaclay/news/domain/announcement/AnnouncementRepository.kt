package com.upsaclay.news.domain.announcement

import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    val announcements: Flow<List<Announcement>>

    val currentAnnouncements: List<Announcement>

    fun getAnnouncementFlow(announcementId: String): Flow<Announcement?>

    fun getAnnouncement(announcementId: String): Announcement?

    suspend fun getRemoteAnnouncements(): List<Announcement>

    suspend fun createAnnouncement(announcement: Announcement)

    suspend fun updateAnnouncement(announcement: Announcement)

    suspend fun upsertLocalAnnouncement(announcement: Announcement)

    suspend fun deleteAnnouncement(announcement: Announcement)

    suspend fun deleteLocalAnnouncement(announcement: Announcement)

    suspend fun deleteLocalAnnouncements()

    suspend fun deleteLocalUserAnnouncements(userId: String)

    suspend fun reportAnnouncement(report: AnnouncementReport)
}