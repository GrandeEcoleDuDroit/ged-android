package com.upsaclay.common.data.remote.api.user

import com.upsaclay.common.data.UserField.Oracle.USER_ID
import com.upsaclay.common.data.UserField.Oracle.USER_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.common.data.remote.model.RemoteUserReport
import com.upsaclay.common.data.remote.model.ServerResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

internal interface UserServerApi {
    @GET("users")
    suspend fun getUsers(): Response<List<OracleUser>>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<OracleUser?>

    @POST("users/create")
    suspend fun createUser(@Body user: OracleUser): Response<ServerResponse>

    @Multipart
    @POST("users/profile-picture/update")
    suspend fun updateProfilePicture(
        @Part image: MultipartBody.Part,
        @Part(USER_ID) userId: RequestBody,
        @Part(USER_PROFILE_PICTURE_FILE_NAME) previousProfilePictureFileName: RequestBody?
    ): Response<ServerResponse>

    @POST("users/delete")
    suspend fun deleteUser(@Body oracleUser: OracleUser): Response<ServerResponse>

    @FormUrlEncoded
    @POST("users/profile-picture/delete")
    suspend fun deleteProfilePicture(
        @Field(USER_ID) userId: String,
        @Field(USER_PROFILE_PICTURE_FILE_NAME) profilePictureFileName: String
    ): Response<ServerResponse>

    @POST("users/report")
    suspend fun reportUser(@Body report: RemoteUserReport): Response<ServerResponse>
}