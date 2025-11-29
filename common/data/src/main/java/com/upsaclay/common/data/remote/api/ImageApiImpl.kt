package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.model.ServerResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

internal class ImageApiImpl(
    private val serverImageApi: ServerImageApi
): ImageApi {
    override suspend fun getImage(url: String): okhttp3.Response {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        return client.newCall(request).execute()
    }

    override suspend fun uploadImage(imageFile: File) {
        val requestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        mapServerResponseException(
            message = "Failed to upload image with Server",
            block = { serverImageApi.uploadImage(multipartBody) }
        )
    }

    override suspend fun deleteImage(fileName: String) {
        mapServerResponseException(
            message = "Failed to delete image with Server",
            block = { serverImageApi.deleteImage(fileName) }
        )
    }

    internal interface ServerImageApi {
        @Multipart
        @POST("image/upload")
        suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ServerResponse>

        @DELETE("image/{fileName}")
        suspend fun deleteImage(@Path("fileName") fileName: String): Response<ServerResponse>
    }
}