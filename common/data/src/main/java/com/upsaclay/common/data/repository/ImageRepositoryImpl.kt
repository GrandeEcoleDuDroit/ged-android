package com.upsaclay.common.data.repository

import com.upsaclay.common.data.UrlUtils
import com.upsaclay.common.data.local.ImageLocalDataSource
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.domain.repository.ImageRepository
import java.io.File
import java.io.InputStream

internal class ImageRepositoryImpl(
    private val imageLocalDataSource: ImageLocalDataSource,
    private val imageRemoteDataSource: ImageRemoteDataSource
): ImageRepository {
    override suspend fun getRemoteImage(fileName: String): InputStream? =
        imageRemoteDataSource.getImage(fileName)

    override fun getFileExtension(uri: String): String = imageLocalDataSource.getFileExtension(uri)

    override suspend fun createLocalImage(folderName: String, fileName: String, uri: String): File? =
        imageLocalDataSource.createLocalImage(folderName, fileName, uri)

    override suspend fun createCacheImage(fileName: String, uri: String): File? =
        imageLocalDataSource.createCacheImage(fileName, uri)

    override suspend fun uploadImage(file: File) {
        imageRemoteDataSource.uploadImage(file)
    }

    override suspend fun deleteRemoteImage(url: String) {
        UrlUtils.extractFileNameFromUrl(url)?.let {
            imageRemoteDataSource.deleteImage(it)
        }
    }

    override suspend fun deleteLocalImage(folderName: String, fileName: String) {
        imageLocalDataSource.deleteLocalImage(folderName, fileName)
    }

    override suspend fun deleteCacheImage(fileName: String) {
        imageLocalDataSource.deleteCacheImage(fileName)
    }
}