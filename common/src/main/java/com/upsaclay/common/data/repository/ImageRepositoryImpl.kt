package com.upsaclay.common.data.repository

import com.upsaclay.common.data.remote.api.ImageRemoteApi
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.utils.errorLog
import com.upsaclay.common.utils.infoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class ImageRepositoryImpl(private val imageRemoteApi: ImageRemoteApi): ImageRepository {
    override suspend fun downloadImageWithServer(fileName: String): ByteArray? =
        withContext(Dispatchers.IO) {
            try {
                val response = imageRemoteApi.downloadImage(fileName)

                if (response.isSuccessful && response.body() != null) {
                    response.body()!!.bytes()
                }
                else {
                errorLog(response.errorBody().toString())
                null
                }
            } catch (e: Exception) {
                errorLog("Error to download image from server: ${e.message}", e)
                null
            }
    }

    override suspend fun uploadImage(file: File): Result<String> = withContext(Dispatchers.IO) {
        try {
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
            val uploadImageResponse = imageRemoteApi.uploadImage(multipartBody)
            val responseBody = uploadImageResponse.body()

            if (uploadImageResponse.isSuccessful) {
                val successMessage = responseBody?.message ?: "Upload image ${file.name} successful"
                infoLog(successMessage)
                Result.success(successMessage)
            }
            else {
                val errorMessage = responseBody?.error
                    ?: "Error upload image ${file.name}, HTTP status: ${uploadImageResponse.code()}"
                errorLog(errorMessage)
                Result.failure(IOException(errorMessage))
            }
        } catch (e: Exception) {
            errorLog("Error upload image: ${e.message}", e)
            Result.failure(e)
        }
    }
}