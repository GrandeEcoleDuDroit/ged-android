package com.upsaclay.news.data.remote

import android.util.Log
import com.upsaclay.news.data.model.Announcement

class AnnouncementRemoteDataSource(
    private val announcementApi: AnnouncementApi
) {
    suspend fun getAllAnnouncement(): List<Announcement> {
        return try {
            val announcementResponse = announcementApi.getAllAnnouncement()
            val announcementsWithUserDTO = announcementResponse.body().takeIf {
                announcementResponse.isSuccessful && it != null
            } ?: emptyList()

            announcementsWithUserDTO.map { it.toAnnouncement() }
        }
        catch (e: Exception){
            Log.e("AnnouncementRemoteDataSource", "Error to get all announcement: ${e.message.toString()}")
            emptyList()
        }
    }
}