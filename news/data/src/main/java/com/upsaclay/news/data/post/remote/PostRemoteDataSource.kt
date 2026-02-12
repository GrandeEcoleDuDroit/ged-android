package com.upsaclay.news.data.post.remote

import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.news.data.post.toPost
import com.upsaclay.news.data.post.toRemote
import com.upsaclay.news.domain.post.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class PostRemoteDataSource(private val postApi: PostApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getPosts(): List<Post> {
        return try {
            postApi.getPosts()?.map { it.toPost() } ?: emptyList()
        } catch (e: Exception) {
            throw mapServerException(e)
        }
    }

    suspend fun createPost(post: Post, imageFiles: List<File>) {
        withContext(dispatcher) {
            try {
                postApi.createPost(post.toRemote(), imageFiles)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}