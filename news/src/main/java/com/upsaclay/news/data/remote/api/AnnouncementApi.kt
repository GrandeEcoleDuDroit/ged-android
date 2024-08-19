package com.upsaclay.news.data.remote.api

import com.upsaclay.news.data.remote.model.AnnouncementWithUserDTO
import retrofit2.Response
import retrofit2.http.GET

interface AnnouncementApi {
    @GET("announcements")
    suspend fun getAllAnnouncement(): Response<List<AnnouncementWithUserDTO>>
}