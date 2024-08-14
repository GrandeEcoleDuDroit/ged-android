package com.upsaclay.authentication.data.remote

import com.upsaclay.common.data.model.ServerResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticationParisSaclayApi {
    @FormUrlEncoded
    @POST("/v1/auth/signin")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("state") hash: String
    ): Response<ServerResponse.EmptyResponse>
}