package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.ImageRetrofitApi
import com.upsaclay.common.domain.model.ServerResponse
import com.upsaclay.common.utils.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.File

internal class ImageRemoteDataSource(private val imageRetrofitApi: ImageRetrofitApi) {
    suspend fun uploadImage(file: File): Response<ServerResponse.EmptyResponse> = withContext(Dispatchers.IO) {
        try {
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
            imageRetrofitApi.uploadImage(multipartBody)
        } catch (e: Exception) {
            e("Error uploading image: ${e.message}", e)
            Response.error(500, e.message.toString().toResponseBody())
        }
    }

    suspend fun deleteImage(imageName: String): Response<ServerResponse.EmptyResponse> = withContext(Dispatchers.IO) {
        try {
            imageRetrofitApi.deleteImage(imageName)
        } catch (e: Exception) {
            e("Error deleting image: ${e.message.toString()}", e)
            Response.error(500, e.message.toString().toResponseBody())
        }
    }
}