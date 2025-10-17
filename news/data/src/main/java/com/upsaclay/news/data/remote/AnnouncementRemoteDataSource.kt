package com.upsaclay.news.data.remote

import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.data.toAnnouncement
import com.upsaclay.news.data.toRemote
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AnnouncementRemoteDataSource(private val announcementApi: AnnouncementApi) {
    suspend fun getAnnouncement(): List<Announcement> = withContext(Dispatchers.IO) {
        mapServerResponseException(
            message = "Failed to fetch announcements",
            block = { announcementApi.getAnnouncements() }
        )?.map { it.toAnnouncement() } ?: emptyList()
    }

    suspend fun createAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                message = "Failed to create announcement",
                block = { announcementApi.createAnnouncement(announcement.toRemote()) }
            )
        }
    }

    suspend fun deleteAnnouncements(userId: String) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                message = "Failed to delete announcements",
                block = { announcementApi.deleteAnnouncements(userId) }
            )
        }
    }

    suspend fun deleteAnnouncement(id: String) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                message = "Failed to delete announcement",
                block = { announcementApi.deleteAnnouncement(id) }
            )
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                message = "Failed to update announcement",
                block = { announcementApi.updateAnnouncement(announcement.toRemote()) }
            )
        }
    }

    suspend fun reportAnnouncement(report: AnnouncementReport) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                message = "Failed to report announcement",
                block = {
                    announcementApi.reportAnnouncement(report.toRemote())
                }
            )
        }
    }
}