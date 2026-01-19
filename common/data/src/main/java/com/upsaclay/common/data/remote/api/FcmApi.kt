package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.remote.model.ServerResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FcmApi {
    @FormUrlEncoded
    @POST("fcm/add-token")
    suspend fun addToken(
        @Field("userId") userId: String,
        @Field("token") token: String
    ): Response<ServerResponse>

    @FormUrlEncoded
    @POST("fcm/delete-token")
    suspend fun deleteToken(
        @Field("userId") userId: String,
        @Field("token") token: String
    ): Response<ServerResponse>

    @FormUrlEncoded
    @POST("fcm/send-notification")
    suspend fun sendNotification(
        @Field("userId") userId: String,
        @Field("recipientId") recipientId: String,
        @Field("fcmMessage") fcmMessage: String
    ): Response<ServerResponse>
}