package com.upsaclay.news.data.announcement

import com.upsaclay.common.data.utils.e
import com.upsaclay.news.data.announcement.local.AnnouncementLocalDataSource
import com.upsaclay.news.data.announcement.remote.AnnouncementRemoteDataSource
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.AnnouncementReport
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class AnnouncementRepositoryImpl(
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
    private val announcementLocalDataSource: AnnouncementLocalDataSource,
    scope: CoroutineScope
) : AnnouncementRepository {
    private val _announcements = announcementLocalDataSource.getAnnouncements()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    override val announcements: Flow<List<Announcement>> = _announcements

    override val currentAnnouncements: List<Announcement>
        get() = _announcements.value

    override fun getAnnouncementFlow(announcementId: String): Flow<Announcement?> =
        _announcements.map { announcements ->
            announcements.firstOrNull { it.id == announcementId }
        }

    override fun getAnnouncement(announcementId: String): Announcement? =
        _announcements.value.firstOrNull { it.id == announcementId }

    override suspend fun getRemoteAnnouncements(): List<Announcement> {
        return try {
            announcementRemoteDataSource.getAnnouncement()
        } catch (e: Exception) {
            e("Error getting remote announcements", e)
            throw e
        }
    }

    override suspend fun createAnnouncement(announcement: Announcement) {
        try {
            announcementLocalDataSource.upsertAnnouncement(announcement)
            announcementRemoteDataSource.createAnnouncement(announcement)
        } catch (e: Exception) {
            e("Error creating announcement ${announcement.id}", e)
            throw e
        }
    }

    override suspend fun updateAnnouncement(announcement: Announcement) {
        try {
            announcementRemoteDataSource.updateAnnouncement(announcement)
            announcementLocalDataSource.upsertAnnouncement(announcement)
        } catch (e: Exception) {
            e("Error updating announcement ${announcement.id}", e)
            throw e
        }
    }

    override suspend fun upsertLocalAnnouncement(announcement: Announcement) {
        announcementLocalDataSource.upsertAnnouncement(announcement)
    }

    override suspend fun deleteAnnouncement(announcement: Announcement) {
        try {
            announcementRemoteDataSource.deleteAnnouncement(announcement)
            announcementLocalDataSource.deleteAnnouncement(announcement)
        } catch (e: Exception) {
            e("Error deleting announcement ${announcement.id}", e)
            throw e
        }
    }

    override suspend fun deleteLocalAnnouncement(announcement: Announcement) {
        announcementLocalDataSource.deleteAnnouncement(announcement)
    }

    override suspend fun deleteLocalAnnouncements() {
        announcementLocalDataSource.deleteAnnouncements()
    }

    override suspend fun deleteLocalUserAnnouncements(userId: String) {
        announcementLocalDataSource.deleteUserAnnouncements(userId)
    }

    override suspend fun reportAnnouncement(report: AnnouncementReport) {
        try {
            announcementRemoteDataSource.reportAnnouncement(report)
        } catch (e: Exception) {
            e("Error reporting announcement ${report.announcementId}", e)
            throw e
        }
    }
}