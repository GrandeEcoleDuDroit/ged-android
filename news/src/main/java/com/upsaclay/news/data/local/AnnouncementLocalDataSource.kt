package com.upsaclay.news.data.local

import com.upsaclay.news.domain.model.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AnnouncementLocalDataSource(
    private val announcementDao: AnnouncementDao
) {
    suspend fun getAllAnnouncements(): Flow<List<Announcement>> = withContext(Dispatchers.IO) {
        announcementDao.getAllAnnouncements().map { announcementsList ->
            announcementsList.map { it.toAnnouncement() }
        }
    }

    suspend fun upsertAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            val announcementEntity = AnnouncementEntity.fromAnnouncement(announcement)
            announcementDao.upsertAnnouncement(announcementEntity)
        }
    }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            val announcementEntity = AnnouncementEntity.fromAnnouncement(announcement)
            announcementDao.deleteAnnouncement(announcementEntity)
        }
    }
}