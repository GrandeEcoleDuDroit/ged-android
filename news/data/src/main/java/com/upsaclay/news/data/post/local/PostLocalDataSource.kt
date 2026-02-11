package com.upsaclay.news.data.post.local

import com.upsaclay.news.data.post.toLocal
import com.upsaclay.news.domain.post.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostLocalDataSource(private val postDao: PostDao) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun upsertPost(post: Post) {
        withContext(dispatcher) {
            postDao.upsertPost(post.toLocal())
        }
    }
}