package com.upsaclay.common.data.repository

import com.upsaclay.common.data.remote.api.ImageRemoteApi
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.utils.errorLog
import com.upsaclay.common.utils.infoLog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class ImageRepositoryImpl(private val imageRemoteApi: ImageRemoteApi): ImageRepository {
    override suspend fun downloadImageWithServer(fileName: String): ByteArray? {
        val response = imageRemoteApi.downloadImage(fileName)

        return if(response.isSuccessful && response.body() != null) {
            response.body()!!.bytes()
        }
        else {
            errorLog(response.errorBody().toString())
            null
        }
    }

    override suspend fun uploadImage(file: File): Result<String> {
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
        val uploadImageResponse = imageRemoteApi.uploadImage(multipartBody)
        val responseBody = uploadImageResponse.body()

        return if(uploadImageResponse.isSuccessful) {
            infoLog(responseBody?.message ?: "Upload image ${file.name} successful")
            Result.success("Upload image ${file.name} successful")
        }
        else {
            errorLog(responseBody?.error ?: "Error upload image ${file.name}")
            Result.failure(IOException("Error to upload image ${file.name}"))
        }
    }
}