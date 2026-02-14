package com.upsaclay.news.domain.post

import kotlinx.coroutines.flow.Flow
import java.io.File

interface PostRepository {
    val posts: Flow<List<Post>>

    suspend fun getLocalPosts(): List<Post>

    suspend fun getRemotePosts(): List<Post>

    suspend fun getLocalPost(postId: String): Post?

    suspend fun createPost(post: Post, imageFiles: List<File>)

    suspend fun updatePost(post: Post, imageFiles: List<File>)

    suspend fun upsertLocalPost(post: Post)

    suspend fun deletePost(post: Post)

    suspend fun deleteLocalPost(post: Post)
}