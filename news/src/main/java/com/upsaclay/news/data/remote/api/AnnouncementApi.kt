package com.upsaclay.news.data.remote.api

import com.upsaclay.common.data.model.ServerResponse.EmptyResponse
import com.upsaclay.common.data.model.ServerResponse.IntResponse
import com.upsaclay.news.data.remote.model.AnnouncementDTO
import com.upsaclay.news.data.remote.model.AnnouncementWithUserDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AnnouncementApi {
    @GET("announcements")
    suspend fun getAllAnnouncement(): Response<List<AnnouncementWithUserDTO>>

    @POST("announcements/create")
    suspend fun createAnnouncement(announcementDTO: AnnouncementDTO): Response<IntResponse>

    @DELETE("announcements/{id}")
    suspend fun deleteAnnouncement(@Path("id") id: Int): Response<EmptyResponse>

    @POST("announcements/update")
    suspend fun updateAnnouncement(announcementDTO: AnnouncementDTO): Response<EmptyResponse>
}