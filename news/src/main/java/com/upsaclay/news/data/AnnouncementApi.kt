package com.upsaclay.news.data

import com.upsaclay.news.data.model.AnnouncementWithUserDTO
import retrofit2.Response
import retrofit2.http.GET

interface AnnouncementApi {
    @GET("/announcement")
    suspend fun getAllAnnouncement(): Response<List<AnnouncementWithUserDTO>>
}