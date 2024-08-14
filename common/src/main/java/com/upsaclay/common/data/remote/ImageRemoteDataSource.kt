package com.upsaclay.common.data.remote

import com.upsaclay.common.data.model.ServerResponse
import com.upsaclay.common.data.remote.api.ImageRemoteApi
import com.upsaclay.common.utils.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.File

class ImageRemoteDataSource(private val imageRemoteApi: ImageRemoteApi) {

    suspend fun downloadImage(fileName: String): ByteArray? = withContext(Dispatchers.IO) {
        try{
            val response = imageRemoteApi.downloadImage(fileName)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.bytes()
            }
            else {
                e(response.errorBody().toString())
                null
            }
        } catch (e: Exception) {
            e("Error to download image from server: ${e.message}", e)
            null
        }
    }

    suspend fun uploadImage(file: File): Response<ServerResponse.EmptyResponse> = withContext(Dispatchers.IO) {
        try {
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
            imageRemoteApi.uploadImage(multipartBody)
        } catch (e: Exception) {
            e("Error upload image: ${e.message}", e)
            Response.error(500, e.message.toString().toResponseBody())
        }
    }

    suspend fun deleteImage(imageName: String): Response<ServerResponse.EmptyResponse> = withContext(Dispatchers.IO) {
        try {
            imageRemoteApi.deleteImage(imageName)
        } catch (e: Exception) {
            e("Error to delete image: ${e.message.toString()}", e)
            Response.error(500, e.message.toString().toResponseBody())
        }
    }
}