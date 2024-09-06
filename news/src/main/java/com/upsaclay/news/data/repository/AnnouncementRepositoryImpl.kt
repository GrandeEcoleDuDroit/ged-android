package com.upsaclay.news.data.repository

import com.upsaclay.news.data.local.AnnouncementLocalDataSource
import com.upsaclay.news.data.remote.AnnouncementRemoteDataSource
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class AnnouncementRepositoryImpl(
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
    private val announcementLocalDataSource: AnnouncementLocalDataSource
): AnnouncementRepository {
    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    override val announcements: Flow<List<Announcement>> = _announcements

    init {
        CoroutineScope(Dispatchers.IO).launch {
            announcementLocalDataSource.getAllAnnouncements().collect {
                _announcements.value = it
            }
        }
    }

    override suspend fun refreshAnnouncements() {
        val remoteAnnouncements = announcementRemoteDataSource.getAllAnnouncement()
        if (remoteAnnouncements.isNotEmpty()) {
            val localAnnouncements = announcementLocalDataSource.getAllAnnouncements().first()

            val announcementsToUpdate = remoteAnnouncements.filterNot { remoteValue ->
                localAnnouncements.any { localValue ->
                    localValue.id == remoteValue.id && localValue.date == remoteValue.date
                }
            }

            announcementsToUpdate.forEach { announcementLocalDataSource.upsertAnnouncement(it) }
        }
    }

    override suspend fun createAnnouncement(announcement: Announcement): Result<Int> {
        return announcementRemoteDataSource.createAnnouncement(announcement)
            .onSuccess { announcementId ->
                announcementLocalDataSource.upsertAnnouncement(announcement.copy(id = announcementId))
            }
    }

    override suspend fun deleteAnnouncement(announcement: Announcement): Result<Unit> {
        return announcementRemoteDataSource.deleteAnnouncement(announcement.id)
            .onSuccess {
                announcementLocalDataSource.deleteAnnouncement(announcement)
            }
    }

    override suspend fun updateAnnouncement(announcement: Announcement): Result<Unit> {
        return announcementRemoteDataSource.updateAnnouncement(announcement)
            .onSuccess {
                announcementLocalDataSource.upsertAnnouncement(announcement)
            }
    }
}