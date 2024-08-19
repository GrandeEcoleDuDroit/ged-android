package com.upsaclay.news.data.remote

import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.domain.model.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber.Forest.e

class AnnouncementRemoteDataSource(
    private val announcementApi: AnnouncementApi
) {
    suspend fun getAllAnnouncement(): List<Announcement> = withContext(Dispatchers.IO) {
        try {
            val announcementResponse = announcementApi.getAllAnnouncement()
            val announcementsWithUserDTO = announcementResponse.body().takeIf {
                announcementResponse.isSuccessful && it != null
            } ?: emptyList()

            announcementsWithUserDTO.map { it.toAnnouncement() }
        }
        catch (e: Exception){
            e("Error to get all remote announcements: %s", e.message.toString())
            emptyList()
        }
    }
}