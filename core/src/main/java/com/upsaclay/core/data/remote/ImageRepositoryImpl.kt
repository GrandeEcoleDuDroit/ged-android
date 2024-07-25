package com.upsaclay.core.data.remote

import com.upsaclay.core.utils.errorLog
import com.upsaclay.core.utils.infoLog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImageRepositoryImpl(private val imageApi: ImageApi): ImageRepository {
    override suspend fun downloadImage(fileName: String): ByteArray? {
        val response = imageApi.downloadImage(fileName)
        return if(response.isSuccessful && response.body() != null) {
            response.body()!!.bytes()
        }
        else {
            errorLog(response.errorBody().toString())
            null
        }
    }

    override suspend fun uploadImage(file: File) {
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
        val response = imageApi.uploadImage(multipartBody)
        val responseServer = response.body()
        if(response.isSuccessful) {
            infoLog(responseServer?.message ?: "Upload image ${file.name} successful")
        }
        else {
            errorLog(responseServer?.error ?: "Error upload image ${file.name}")
        }
    }
}