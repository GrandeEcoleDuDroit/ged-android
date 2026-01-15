package com.upsaclay.common.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.Request

internal class ImageApiImpl: ImageApi {
    override suspend fun getImage(url: String): okhttp3.Response {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        return client.newCall(request).execute()
    }
}