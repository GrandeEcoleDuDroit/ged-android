package com.upsaclay.news.data.announcement.local

import com.upsaclay.news.data.announcement.toAnnouncement
import com.upsaclay.news.data.announcement.toLocal
import com.upsaclay.news.domain.announcement.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

internal class AnnouncementLocalDataSource(private val announcementDao: AnnouncementDao) {
    fun getAnnouncementsFlow(): Flow<List<Announcement>> =
        announcementDao.getAnnouncementsFlow().map { localAnnouncements ->
            localAnnouncements.map { it.toAnnouncement() }
        }

    fun getAnnouncementFlow(announcementId: String): Flow<Announcement> =
        announcementDao.getAnnouncementFlow(announcementId).mapNotNull { it?.toAnnouncement() }

    suspend fun getAnnouncements(): List<Announcement> = announcementDao.getAnnouncements().map { it.toAnnouncement() }

    suspend fun getAnnouncement(announcementId: String): Announcement? =
        announcementDao.getAnnouncement(announcementId)?.toAnnouncement()

    suspend fun upsertAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.upsertAnnouncement(announcement.toLocal())
        }
    }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.deleteAnnouncement(announcement.toLocal())
        }
    }

    suspend fun deleteAnnouncements() {
        withContext(Dispatchers.IO) {
            announcementDao.deleteAnnouncements()
        }
    }

    suspend fun deleteUserAnnouncements(userId: String) {
        withContext(Dispatchers.IO) {
            announcementDao.deleteUserAnnouncements(userId)
        }
    }
}