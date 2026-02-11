package com.upsaclay.news.domain.post.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.PostRepository
import com.upsaclay.news.domain.post.PostUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreatePostUseCase(
    private val postRepository: PostRepository,
    private val imageRepository: ImageRepository,
    private val scope: CoroutineScope
) {
    fun execute(post: Post, imageUris: List<String>) {
        scope.launch {
            val imagePaths: MutableList<String> = mutableListOf()
            val imageFiles = imageUris.mapNotNull { uri ->
                val extension = imageRepository.getFileExtension(uri)
                val fileName = "${PostUtils.Image.generateFileName(post.id)}.$extension"
                val imagePath = PostUtils.Image.getRelativePath(fileName)
                imagePaths.add(imagePath)
                imageRepository.createLocalImage(imagePath, uri)
            }

            try {
                postRepository.createPost(post.copy(state = PostState.Publishing(imagePaths)), imageFiles)
                postRepository.upsertLocalPost(post.copy(state = PostState.Published(imagePaths)))
                imagePaths.forEach {
                    imageRepository.deleteLocalImage(it)
                }
            } catch (_: Exception) {
                postRepository.upsertLocalPost(post.copy(state = PostState.Error(imagePaths)))
            }
        }
    }
}