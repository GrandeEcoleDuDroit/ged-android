package com.upsaclay.news.data.local

import com.upsaclay.news.domain.model.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnnouncementLocalDataSource(
    private val announcementDao: AnnouncementDao
) {
    suspend fun getAllAnnouncements(): List<Announcement> = withContext(Dispatchers.IO) {
        announcementDao.getAllAnnouncements().map { announcementEntity ->
            announcementEntity.toAnnouncement()
        }
    }

    suspend fun upsertAnnouncement(announcement: Announcement) = withContext(Dispatchers.IO) {
        val announcementEntity = AnnouncementEntity.fromAnnouncement(announcement)
        announcementDao.upsertAnnouncement(announcementEntity)
    }
}