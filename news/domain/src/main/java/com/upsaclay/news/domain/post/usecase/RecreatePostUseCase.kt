package com.upsaclay.news.domain.post.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.PostRepository
import java.io.File

class RecreatePostUseCase(
    private val postRepository: PostRepository,
    private val imageRepository: ImageRepository
) {
    suspend fun execute(post: Post) {
        if (post.state is PostState.Error) {
            val imagePaths = post.state.imagePaths
            val imageFiles = imagePaths.mapNotNull { path ->
                File(path).takeIf { it.exists() }
            }

            try {
                postRepository.createPost(post.copy(state = PostState.Publishing(imagePaths)), imageFiles)
                postRepository.upsertLocalPost(post.copy(state = PostState.Published(imagePaths)))
                imagePaths.forEach {
                    imageRepository.deleteLocalImage(it)
                }
            } catch (e: Exception) {
                println(e)
                postRepository.upsertLocalPost(post.copy(state = PostState.Error(imagePaths)))
            }
        }
    }
}