package com.upsaclay.news.data.post.remote

import java.io.File

interface PostApi {
    suspend fun getPosts(): List<RemotePost>?

    suspend fun createPost(remotePost: RemotePost, imageFiles: List<File>)

    suspend fun updatePost(remotePost: RemotePost, imageFiles: List<File>)

    suspend fun deletePost(postId: String)
}