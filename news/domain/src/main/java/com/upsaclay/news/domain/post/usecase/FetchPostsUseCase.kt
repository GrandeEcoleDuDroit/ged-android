package com.upsaclay.news.domain.post.usecase

import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.PostRepository

class FetchPostsUseCase(
    private val postRepository: PostRepository,
    private val upsertLocalPostUseCase: UpsertLocalPostUseCase
) {
    suspend fun execute() {
        val localPosts = postRepository.getLocalPosts()
        val remotePosts = postRepository.getRemotePosts()

        val postsToDelete = localPosts.filter { (it.state is PostState.Published && it !in remotePosts) }
        val postToUpsert = remotePosts.filter { it !in localPosts }

        postsToDelete.forEach { postRepository.deleteLocalPost(it) }
        postToUpsert.forEach { upsertLocalPostUseCase.execute(it) }
    }
}