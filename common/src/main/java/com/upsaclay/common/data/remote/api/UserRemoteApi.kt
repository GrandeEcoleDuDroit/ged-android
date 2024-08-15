package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.model.ServerResponse.EmptyResponse
import com.upsaclay.common.data.model.ServerResponse.IntResponse
import com.upsaclay.common.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

internal interface UserRemoteApi {
    @POST("users/create")
    suspend fun createUser(@Body user: UserDTO): Response<IntResponse>

    @FormUrlEncoded
    @POST("users/update/profile-picture-url")
    suspend fun updateProfilePictureUrl(
        @Field("USER_ID") userId: Int,
        @Field("USER_PROFILE_PICTURE_URL") userProfilePictureUrl: String
    ): Response<EmptyResponse>

    @DELETE("users/profile-picture-url/{userId}")
    suspend fun deleteProfilePictureUrl(@Path("userId") userId: Int): Response<EmptyResponse>
}