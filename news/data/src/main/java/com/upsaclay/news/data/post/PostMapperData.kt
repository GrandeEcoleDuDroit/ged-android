package com.upsaclay.news.data.post

import com.google.gson.Gson
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.news.data.post.local.LocalPost
import com.upsaclay.news.data.post.remote.RemotePost
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.PostUtils

fun Post.toLocal() = LocalPost(
    postId = id,
    postTitle = title,
    postContent = content,
    postLink = link,
    postSourceId = source.id,
    postDate = date.toEpochMilliUTC(),
    postImageFileNames = Gson().toJson(extractImageFileNames(state)),
    postState = state.toString()
)

fun Post.toRemote() = RemotePost(
    postId = id,
    postTitle = title,
    postContent = content,
    postLink = link,
    postSourceId = source.id,
    postDate = date.toEpochMilliUTC(),
    postImageFileNames = Gson().toJson(extractImageFileNames(state))
)

private fun extractImageFileNames(state: PostState): List<String> {
    return when (state) {
        is PostState.Draft -> emptyList()
        is PostState.Publishing -> state.imagePaths.mapNotNull(PostUtils.Image::extractFileNameFromPath)
        is PostState.Published -> state.imageUrls.mapNotNull(PostUtils.Image::getFileName)
        is PostState.Error -> state.imagePaths.mapNotNull(PostUtils.Image::extractFileNameFromPath)
    }
}