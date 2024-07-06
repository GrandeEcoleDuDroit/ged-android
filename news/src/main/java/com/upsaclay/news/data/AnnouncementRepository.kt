package com.upsaclay.news.data

import com.upsaclay.news.data.model.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    val announcements: Flow<List<Announcement>>
    suspend fun updateAnnouncements()
}