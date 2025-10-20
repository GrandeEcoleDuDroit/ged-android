package com.upsaclay.common.data.repository

import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.domain.repository.ImageRepository
import java.io.File
import java.io.InputStream

internal class ImageRepositoryImpl(
    private val imageRemoteDataSource: ImageRemoteDataSource
): ImageRepository {
    override suspend fun getImage(fileName: String): InputStream? =
        imageRemoteDataSource.getImage(fileName)

    override suspend fun uploadImage(file: File) {
        imageRemoteDataSource.uploadImage(file)
    }

    override suspend fun deleteImage(fileName: String) {
        imageRemoteDataSource.deleteImage(fileName)
    }
}