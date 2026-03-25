package com.upsaclay.news.domain.post.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.PostRepository

class DeletePostUseCase(
    private val postRepository: PostRepository,
    private val imageRepository: ImageRepository
) {
    suspend fun execute(post: Post) {
        when (val state = post.state) {
            is PostState.Draft -> postRepository.deleteLocalPost(post)

            is PostState.Publishing -> {
                postRepository.deleteLocalPost(post)
                state.imagePaths.forEach {
                    imageRepository.deleteLocalImage(it)
                }
            }

            is PostState.Published -> postRepository.deletePost(post)

            is PostState.Error -> {
                postRepository.deleteLocalPost(post)
                state.imagePaths.forEach {
                    imageRepository.deleteLocalImage(it)
                }
            }
        }
    }
}