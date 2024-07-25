package com.upsaclay.core.data.remote

import com.upsaclay.core.data.model.ServerResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ImageApi {
    @GET("image/download/{filename}")
    suspend fun downloadImage(@Path("filename") filename: String): Response<ResponseBody>

    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ServerResponse.StringServerResponse>
}