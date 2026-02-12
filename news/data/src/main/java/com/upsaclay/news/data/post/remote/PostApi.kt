package com.upsaclay.news.data.post.remote

import java.io.File

interface PostApi {
    suspend fun createPost(remotePost: RemotePost, imageFiles: List<File>)
}