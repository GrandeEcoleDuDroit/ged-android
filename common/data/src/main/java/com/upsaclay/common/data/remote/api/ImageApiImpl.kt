package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.model.ServerResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    override suspend fun uploadImage(imageFile: File, imagePath: String) {
        val requestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        val imagePathBody = imagePath.toRequestBody("text/plain".toMediaType())

        mapServerResponseException(
            message = "Failed to upload image with Server",
            block = { serverImageApi.uploadImage(multipartBody, imagePathBody) }
        )
    }

    override suspend fun deleteImage(imagePath: String) {
        mapServerResponseException(
            message = "Failed to delete image with Server",
            block = { serverImageApi.deleteImage(imagePath) }
        )
    }

    internal interface ServerImageApi {
        @Multipart
        @POST("image/upload")
        suspend fun uploadImage(
            @Part image: MultipartBody.Part,
            @Part("imagePath") imagePath: RequestBody
        ): Response<ServerResponse>

        @FormUrlEncoded
        @POST("image/delete")
        suspend fun deleteImage(@Field("imagePath") imagePath: String): Response<ServerResponse>
    }
}