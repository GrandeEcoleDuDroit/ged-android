package com.upsaclay.news.data.repository

import com.upsaclay.news.data.local.AnnouncementLocalDataSource
import com.upsaclay.news.data.remote.AnnouncementRemoteDataSource
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import com.upsaclay.news.domain.repository.AnnouncementRepository
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

    override suspend fun getRemoteAnnouncements(): List<Announcement> =
        announcementRemoteDataSource.getAnnouncement()

    override suspend fun createAnnouncement(announcement: Announcement) {
        announcementLocalDataSource.upsertAnnouncement(announcement)
        announcementRemoteDataSource.createAnnouncement(announcement)
    }

    override suspend fun updateAnnouncement(announcement: Announcement) {
        announcementRemoteDataSource.updateAnnouncement(announcement)
        announcementLocalDataSource.upsertAnnouncement(announcement)
    }

    override suspend fun upsertLocalAnnouncement(announcement: Announcement) {
        announcementLocalDataSource.upsertAnnouncement(announcement)
    }

    override suspend fun deleteAnnouncements(userId: String) {
//        announcementRemoteDataSource.deleteAnnouncements(userId)
        announcementLocalDataSource.deleteAnnouncements(userId)
    }

    override suspend fun deleteAnnouncement(announcement: Announcement) {
        announcementRemoteDataSource.deleteAnnouncement(announcement)
        announcementLocalDataSource.deleteAnnouncement(announcement)
    }

    override suspend fun deleteLocalAnnouncement(announcement: Announcement) {
        announcementLocalDataSource.deleteAnnouncement(announcement)
    }

    override suspend fun deleteLocalAnnouncements() {
        announcementLocalDataSource.deleteAnnouncements()
    }

    override suspend fun deleteLocalAnnouncements(userId: String) {
        announcementLocalDataSource.deleteAnnouncements(userId)
    }

    override suspend fun reportAnnouncement(report: AnnouncementReport) {
        announcementRemoteDataSource.reportAnnouncement(report)
    }
}