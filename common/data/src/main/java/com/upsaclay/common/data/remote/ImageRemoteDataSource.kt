package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.ImageApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

internal class ImageRemoteDataSource(private val imageApi: ImageApi) {
    suspend fun getImage(fileName: String): InputStream? = withContext(Dispatchers.IO) {
        imageApi.getImage(fileName).body?.byteStream()
    }
}