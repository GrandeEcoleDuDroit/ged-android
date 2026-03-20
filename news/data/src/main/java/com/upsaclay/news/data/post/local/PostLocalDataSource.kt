package com.upsaclay.news.data.post.local

import android.content.Context
import com.upsaclay.news.data.post.toLocal
import com.upsaclay.news.data.post.toPost
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.PostUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class PostLocalDataSource(
    private val context: Context,
    private val postDao: PostDao
) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun getPostsFlow(): Flow<List<Post>> = postDao.getPostsFlow()
        .map { localPosts ->
            localPosts.map { it.toPost(::getImagePath) }
        }

    suspend fun getPosts(): List<Post> = postDao.getPosts()
        .map { it.toPost(::getImagePath) }

    fun getPostFlow(postId: String): Flow<Post?> = postDao.getPostFlow(postId)
        .map { it?.toPost(::getImagePath) }

    suspend fun getPost(postId: String): Post? = postDao.getPost(postId)?.toPost(::getImagePath)

    suspend fun upsertPost(post: Post) {
        withContext(dispatcher) {
            postDao.upsertPost(post.toLocal())
        }
    }

    suspend fun deletePosts() {
        withContext(dispatcher) {
            postDao.deletePosts()
        }
    }

    suspend fun deletePost(post: Post) {
        withContext(dispatcher) {
            postDao.deletePost(post.toLocal())
        }
    }

    private fun getImagePath(fileName: String): String =
        File(context.filesDir, PostUtils.Image.getRelativePath(fileName)).path
}