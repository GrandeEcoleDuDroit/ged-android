package com.upsaclay.news.data

import com.upsaclay.news.data.model.Announcement

class AnnouncementRemoteDataSource(
    private val announcementApi: AnnouncementApi
) {
    suspend fun getAllAnnouncement(): List<Announcement> {
        val announcementResponse = announcementApi.getAllAnnouncement()
        val announcementsWithUserDTO = announcementResponse.body().takeIf {
            announcementResponse.isSuccessful && it != null
        } ?: emptyList()

        return announcementsWithUserDTO.map { it.toDomain() }
    }
}