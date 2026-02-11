package com.upsaclay.news.data.announcement.remote

import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.utils.sendDataServerRequest
import com.upsaclay.common.data.utils.sendServerRequest
import com.upsaclay.news.data.announcement.toAnnouncement
import com.upsaclay.news.data.announcement.toRemote
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.AnnouncementReport
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AnnouncementRemoteDataSource(private val announcementApi: AnnouncementApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getAnnouncement(): List<Announcement> = withContext(dispatcher) {
        try {
            sendDataServerRequest {
                announcementApi.getAnnouncements()
            }?.map { it.toAnnouncement() } ?: emptyList()
        } catch (e: Exception) {
            throw mapServerException(e)
        }
    }

    suspend fun createAnnouncement(announcement: Announcement) {
        withContext(dispatcher) {
            try {
                sendServerRequest { announcementApi.createAnnouncement(announcement.toRemote()) }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        withContext(dispatcher) {
            try {
                sendServerRequest { announcementApi.updateAnnouncement(announcement.toRemote()) }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        withContext(dispatcher) {
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
        withContext(dispatcher) {
            try {
                sendServerRequest { announcementApi.reportAnnouncement(report.toRemote()) }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}