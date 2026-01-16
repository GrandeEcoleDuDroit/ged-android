package com.upsaclay.news.data.remote

import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.utils.sendDataServerRequest
import com.upsaclay.common.data.utils.sendServerRequest
import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.data.toAnnouncement
import com.upsaclay.news.data.toRemote
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AnnouncementRemoteDataSource(private val announcementApi: AnnouncementApi) {
    suspend fun getAnnouncement(): List<Announcement> = withContext(Dispatchers.IO) {
        try {
            sendDataServerRequest {
                announcementApi.getAnnouncements()
            }?.map { it.toAnnouncement() } ?: emptyList()
        } catch (e: Exception) {
            throw mapServerException(e)
        }
    }

    suspend fun createAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            try {
                sendServerRequest { announcementApi.createAnnouncement(announcement.toRemote()) }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            try {
                sendServerRequest { announcementApi.updateAnnouncement(announcement.toRemote()) }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            try {
                sendServerRequest {
                    announcementApi.deleteAnnouncements(announcement.id, announcement.author.id)
                }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun reportAnnouncement(report: AnnouncementReport) {
        withContext(Dispatchers.IO) {
            try {
                sendServerRequest { announcementApi.reportAnnouncement(report.toRemote()) }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}