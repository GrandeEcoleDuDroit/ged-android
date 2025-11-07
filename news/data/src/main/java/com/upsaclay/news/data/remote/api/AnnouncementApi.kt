package com.upsaclay.news.data.remote.api

import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.news.data.remote.model.OutbondRemoteAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncementReport
import com.upsaclay.news.data.remote.model.InboundRemoteAnnouncement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface AnnouncementApi {
    @GET("announcements")
    suspend fun getAnnouncements(): Response<List<InboundRemoteAnnouncement>>

    @POST("announcements/create")
    suspend fun createAnnouncement(@Body outbondRemoteAnnouncement: OutbondRemoteAnnouncement): Response<ServerResponse>

    @DELETE("announcements/user/{userId}")
    suspend fun deleteAnnouncements(@Path("userId") userId: String): Response<ServerResponse>

    @DELETE("announcements/{announcementId}")
    suspend fun deleteAnnouncement(@Path("announcementId") announcementId: String): Response<ServerResponse>

    @POST("announcements/update")
    suspend fun updateAnnouncement(@Body outbondRemoteAnnouncement: OutbondRemoteAnnouncement): Response<ServerResponse>

    @POST("announcements/report")
    suspend fun reportAnnouncement(@Body remoteAnnouncementReport: RemoteAnnouncementReport): Response<ServerResponse>
}