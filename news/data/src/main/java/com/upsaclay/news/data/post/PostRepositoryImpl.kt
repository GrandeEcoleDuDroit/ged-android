package com.upsaclay.news.data.post

import com.upsaclay.common.data.utils.e
import com.upsaclay.news.data.post.local.PostLocalDataSource
import com.upsaclay.news.data.post.remote.PostRemoteDataSource
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.PostRepository
import java.io.File

class PostRepositoryImpl(
    private val postRemoteDataSource: PostRemoteDataSource,
    private val postLocalDataSource: PostLocalDataSource
): PostRepository {
    override suspend fun createPost(post: Post, imageFiles: List<File>) {
        try {
            postLocalDataSource.upsertPost(post)
            postRemoteDataSource.createPost(post, imageFiles)
        } catch (e: Exception) {
            e("Error creating post ${post.id}", e)
            throw e
        }
    }

    override suspend fun upsertLocalPost(post: Post) {
        postLocalDataSource.upsertPost(post)
    }
}