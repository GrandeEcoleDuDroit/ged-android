package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.ImageApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

internal class ImageRemoteDataSource(private val imageApi: ImageApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getImage(fileName: String): InputStream? = withContext(dispatcher) {
        imageApi.getImage(fileName).body?.byteStream()
    }
}