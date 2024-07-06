package com.upsaclay.news.data

import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    suspend fun getLastAnnouncement(): Flow<Announcement>
    suspend fun createAnnouncement(announcement: Announcement)
    suspend fun modifyAnnouncement(announcement: Announcement)
    suspend fun deleteAnnouncement(announcement: Announcement)
}