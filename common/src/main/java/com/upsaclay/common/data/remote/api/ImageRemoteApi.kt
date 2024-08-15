package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.model.ServerResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ImageRemoteApi {
    @GET("image/download/{filename}")
    suspend fun downloadImage(@Path("filename") filename: String): Response<ResponseBody>

    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ServerResponse.EmptyResponse>

    @DELETE("image/{filename}")
    suspend fun deleteImage(@Path("filename") filename: String): Response<ServerResponse.EmptyResponse>
}