package com.upsaclay.news.domain.post.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.PostRepository

class UpsertLocalPostUseCase(
    private val postRepository: PostRepository,
    private val imageRepository: ImageRepository
) {
    suspend fun execute(post: Post) {
        val localPost = postRepository.getLocalPost(post.id)
        postRepository.upsertLocalPost(post)
        localPost?.state?.resolveImagePaths()?.let { paths ->
            paths.forEach {
                imageRepository.deleteLocalImage(it)
            }
        }
    }
}