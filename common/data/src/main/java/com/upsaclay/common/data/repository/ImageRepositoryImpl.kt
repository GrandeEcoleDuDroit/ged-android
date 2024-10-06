package com.upsaclay.common.data.repository

import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.i
import com.upsaclay.common.domain.repository.ImageRepository
import java.io.File
import java.io.IOException

internal class ImageRepositoryImpl(private val imageRemoteDataSource: ImageRemoteDataSource) : ImageRepository {

    override suspend fun uploadImage(file: File): Result<Unit> {
        val response = imageRemoteDataSource.uploadImage(file)

        return if (response.isSuccessful) {
            val successMessage = response.body()?.message ?: "Image ${file.name} uploaded successful"
            i(successMessage)
            Result.success(Unit)
        } else {
            val errorMessage = formatHttpError("Error to upload image ${file.name}", response)
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    override suspend fun deleteImage(fileName: String): Result<Unit> {
        val response = imageRemoteDataSource.deleteImage(fileName)

        return if (response.isSuccessful) {
            val successMessage = response.body()?.message ?: "Image $fileName deleted successfully"
            i(successMessage)
            Result.success(Unit)
        } else {
            val errorMessage = formatHttpError("Error to delete image $fileName", response)
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }
}