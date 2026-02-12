package com.upsaclay.news.data.post

import com.upsaclay.common.data.utils.e
import com.upsaclay.news.data.post.local.PostLocalDataSource
import com.upsaclay.news.data.post.remote.PostRemoteDataSource
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.PostRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class PostRepositoryImpl(
    private val postRemoteDataSource: PostRemoteDataSource,
    private val postLocalDataSource: PostLocalDataSource
): PostRepository {
    private val _posts = postLocalDataSource.getPostsFlow()
    override val posts: Flow<List<Post>> = _posts

    override suspend fun getLocalPosts(): List<Post> = postLocalDataSource.getPosts()

    override suspend fun getRemotePosts(): List<Post> = postRemoteDataSource.getPosts()

    override suspend fun getLocalPost(postId: String): Post? = postLocalDataSource.getPost(postId)

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

    override suspend fun deleteLocalPost(post: Post) {
        postLocalDataSource.deletePost(post)
    }
}