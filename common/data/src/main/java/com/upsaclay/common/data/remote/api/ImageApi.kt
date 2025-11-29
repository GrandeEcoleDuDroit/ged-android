package com.upsaclay.common.data.remote.api

import okhttp3.Response
import java.io.File

interface ImageApi {
    suspend fun getImage(url: String): Response

    suspend fun uploadImage(imageFile: File)

    suspend fun deleteImage(fileName: String)
}