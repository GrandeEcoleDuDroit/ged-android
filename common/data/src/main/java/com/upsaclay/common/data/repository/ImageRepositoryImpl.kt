package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.ImageLocalDataSource
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.domain.entity.FileInformation
import com.upsaclay.common.domain.repository.ImageRepository
import java.io.File
import java.io.InputStream

internal class ImageRepositoryImpl(
    private val imageLocalDataSource: ImageLocalDataSource,
    private val imageRemoteDataSource: ImageRemoteDataSource
): ImageRepository {
    override suspend fun getRemoteImage(fileName: String): InputStream? =
        imageRemoteDataSource.getImage(fileName)

    override fun getFileInformation(uri: String): FileInformation = imageLocalDataSource.getFileInformation(uri)

    override fun getFileExtension(uri: String): String = imageLocalDataSource.getFileExtension(uri)

    override suspend fun createLocalImage(imagePath: String, uri: String): File? =
        imageLocalDataSource.createLocalImage(imagePath, uri)

    override suspend fun createCacheImage(fileName: String, uri: String): File? =
        imageLocalDataSource.createCacheImage(fileName, uri)

    override suspend fun deleteLocalImage(imagePath: String) {
        imageLocalDataSource.deleteLocalImage(imagePath)
    }

    override suspend fun deleteCacheImage(imagePath: String) {
        imageLocalDataSource.deleteCacheImage(imagePath)
    }
}