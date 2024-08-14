package com.upsaclay.common.data.repository

import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.utils.e
import com.upsaclay.common.utils.i
import java.io.File
import java.io.IOException

class ImageRepositoryImpl(
    private val imageRemoteDataSource: ImageRemoteDataSource
): ImageRepository {

    override suspend fun downloadImageWithServer(fileName: String): ByteArray? {
        return imageRemoteDataSource.downloadImage(fileName)
    }

    override suspend fun uploadImage(file: File): Result<String> {
       val response = imageRemoteDataSource.uploadImage(file)
        val responseBody = response.body()

        return if (response.isSuccessful) {
            val successMessage = responseBody?.message ?: "Upload image ${file.name} successful"
            i(successMessage)
            Result.success(successMessage)
        }
        else {
            val errorMessage = responseBody?.error
                ?: "Error upload image ${file.name}, HTTP status: ${response.code()}"
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    override suspend fun deleteImage(fileName: String): Result<Unit> {
        val response = imageRemoteDataSource.deleteImage(fileName)
        return if(response.isSuccessful) {
            Result.success(Unit)
        } else {
            val errorMessage = response.body()?.error
                ?: "Error to delete image ${fileName}, HTTP status: ${response.code()}, ${response.errorBody()}"
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }
}