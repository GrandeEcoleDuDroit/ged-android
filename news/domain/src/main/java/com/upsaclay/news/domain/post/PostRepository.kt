package com.upsaclay.news.domain.post

import java.io.File

interface PostRepository {
    suspend fun createPost(post: Post, imageFiles: List<File>)

    suspend fun upsertLocalPost(post: Post)
}