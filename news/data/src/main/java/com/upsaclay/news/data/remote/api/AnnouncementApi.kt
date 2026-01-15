package com.upsaclay.news.data.remote.api

import com.upsaclay.common.data.UserField.Oracle.USER_ID
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.news.data.AnnouncementField.Remote.ANNOUNCEMENT_ID
import com.upsaclay.news.data.remote.model.InboundRemoteAnnouncement
import com.upsaclay.news.data.remote.model.OutbondRemoteAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncementReport
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

internal interface AnnouncementApi {
    @GET("announcements")
    suspend fun getAnnouncements(): Response<List<InboundRemoteAnnouncement>>

    @POST("announcements/create")
    suspend fun createAnnouncement(@Body remoteAnnouncement: OutbondRemoteAnnouncement): Response<ServerResponse>

    @POST("announcements/update")
    suspend fun updateAnnouncement(@Body remoteAnnouncement: OutbondRemoteAnnouncement): Response<ServerResponse>

    @FormUrlEncoded
    @POST("announcements/delete")
    suspend fun deleteAnnouncements(
        @Field(ANNOUNCEMENT_ID) announcementId: String,
        @Field(USER_ID) authorId: String
    ): Response<ServerResponse>

    @POST("announcements/report")
    suspend fun reportAnnouncement(@Body remoteAnnouncementReport: RemoteAnnouncementReport): Response<ServerResponse>
}