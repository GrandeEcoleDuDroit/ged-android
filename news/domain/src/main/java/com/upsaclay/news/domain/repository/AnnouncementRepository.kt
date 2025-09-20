package com.upsaclay.news.domain.repository

import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    val announcements: Flow<List<Announcement>>

    fun getAnnouncementFlow(announcementId: String): Flow<Announcement?>

    suspend fun getRemoteAnnouncements(): List<Announcement>

    suspend fun createAnnouncement(announcement: Announcement)

    suspend fun updateAnnouncement(announcement: Announcement)

    suspend fun updateLocalAnnouncement(announcement: Announcement)

    suspend fun upsertLocalAnnouncement(announcement: Announcement)

    suspend fun deleteAnnouncement(announcement: Announcement)

    suspend fun deleteLocalAnnouncement(announcement: Announcement)

    suspend fun deleteLocalUserAnnouncements(userId: String)

    suspend fun reportAnnouncement(report: AnnouncementReport)
}