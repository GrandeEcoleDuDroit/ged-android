package com.upsaclay.news.data.post.remote

import com.google.gson.Gson
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.common.data.utils.sendDataServerRequest
import com.upsaclay.common.data.utils.sendServerRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

class PostApiImpl(private val postServerApi: PostServerApi): PostApi {
    private val gson = Gson()

    override suspend fun getPosts(): List<RemotePost>? = sendDataServerRequest {
        postServerApi.getPosts()
    }

    override suspend fun createPost(remotePost: RemotePost, imageFiles: List<File>) {
        val postPart = gson
            .toJson(remotePost)
            .toRequestBody("application/json".toMediaType())

        val imageParts: MutableList<MultipartBody.Part> = mutableListOf()
        imageFiles.forEach {
            val requestBody = it.asRequestBody("image/*".toMediaType())
            val part = MultipartBody.Part.createFormData("images", it.name, requestBody)
            imageParts.add(part)
        }

        sendServerRequest {
            postServerApi.createPost(imageParts, postPart)
        }
    }
}

interface PostServerApi {
    @GET("posts")
    suspend fun getPosts(): Response<List<RemotePost>>

    @Multipart
    @POST("posts/create")
    suspend fun createPost(
        @Part images: List<MultipartBody.Part>,
        @Part("post") post: RequestBody
    ): Response<ServerResponse>
}