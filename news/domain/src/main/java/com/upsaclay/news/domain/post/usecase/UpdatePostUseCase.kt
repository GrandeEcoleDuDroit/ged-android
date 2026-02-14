package com.upsaclay.news.domain.post.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.ImageReference
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.PostRepository
import com.upsaclay.news.domain.post.PostUtils

class UpdatePostUseCase(
    private val postRepository: PostRepository,
    private val imageRepository: ImageRepository
) {
    suspend fun execute(post: Post, imageReferences: List<ImageReference>) {
        var postToUpdate = post
        val imagePaths = mutableListOf<String>()
        val imageUris = imageReferences.filterIsInstance<ImageReference.ImageUri>()
        val imageUrls = imageReferences.filterIsInstance<ImageReference.ImageUrl>()

        val imageFiles = imageUris.mapNotNull { uriReference ->
                val extension = imageRepository.getFileExtension(uriReference.value)
                val fileName = "${PostUtils.Image.generateFileName(post.id)}.$extension"
                val imagePath = PostUtils.Image.getRelativePath(fileName)
                imagePaths.add(imagePath)
                imageRepository.createCacheImage(imagePath, uriReference.value)
            }

        postToUpdate = postToUpdate.copy(
            state = Post.PostState.Published(imageUrls.map { it.value } + imagePaths)
        )

        postRepository.updatePost(postToUpdate, imageFiles)
        imagePaths.forEach {
            imageRepository.deleteCacheImage(it)
        }
    }
}