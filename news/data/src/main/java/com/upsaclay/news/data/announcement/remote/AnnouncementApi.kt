package com.upsaclay.news.data.announcement.remote

import com.upsaclay.common.data.remote.model.ServerResponse
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
    suspend fun createAnnouncement(@Body remoteAnnouncement: OutbondRemoteAnnouncement): Response<ServerResponse>

    @POST("announcements/update")
    suspend fun updateAnnouncement(@Body remoteAnnouncement: OutbondRemoteAnnouncement): Response<ServerResponse>

    @DELETE("announcements/{announcementId}")
    suspend fun deleteAnnouncement(@Path("announcementId") announcementId: String): Response<ServerResponse>

    @POST("announcements/report")
    suspend fun reportAnnouncement(@Body remoteAnnouncementReport: RemoteAnnouncementReport): Response<ServerResponse>
}