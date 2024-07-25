package com.upsaclay.news.data

import com.upsaclay.news.data.model.Announcement
import com.upsaclay.news.data.remote.AnnouncementRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AnnouncementRepositoryImpl(
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource
): AnnouncementRepository {
    private val _announcements: MutableStateFlow<List<Announcement>> = MutableStateFlow(emptyList())
    override val announcements: Flow<List<Announcement>> = _announcements
    override suspend fun updateAnnouncements() {
        _announcements.value = announcementRemoteDataSource.getAllAnnouncement()
    }
}