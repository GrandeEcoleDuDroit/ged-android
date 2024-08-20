package com.upsaclay.news.data.repository

import com.upsaclay.news.data.local.AnnouncementLocalDataSource
import com.upsaclay.news.data.remote.AnnouncementRemoteDataSource
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AnnouncementRepositoryImpl(
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
    private val announcementLocalDataSource: AnnouncementLocalDataSource
): AnnouncementRepository {
    private val _announcements: MutableStateFlow<List<Announcement>> = MutableStateFlow(emptyList())
    override val announcements: Flow<List<Announcement>> = _announcements

    override suspend fun refreshAnnouncements() {
        val remoteAnnouncements = announcementRemoteDataSource.getAllAnnouncement()
        if (remoteAnnouncements.isNotEmpty()) {
            _announcements.value = remoteAnnouncements
            val localAnnouncements = announcementLocalDataSource.getAllAnnouncements()

            val announcementsToUpdate = remoteAnnouncements.filter { remote ->
                localAnnouncements.any { local ->
                    local.id == remote.id && local.date != remote.date
                }
            }

            announcementsToUpdate.forEach { announcementLocalDataSource.upsertAnnouncement(it) }
        } else {
            _announcements.value = announcementLocalDataSource.getAllAnnouncements()
        }
    }
}