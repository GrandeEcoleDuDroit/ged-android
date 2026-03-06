package com.upsaclay.news.data.post

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upsaclay.common.data.toRemote
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.news.data.post.local.LocalPost
import com.upsaclay.news.data.post.remote.RemotePost
import com.upsaclay.news.data.post.remote.RemotePostReport
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.PostReport
import com.upsaclay.news.domain.post.PostUtils

private val gson = Gson()

fun Post.toLocal(): LocalPost = LocalPost(
    postId = id,
    postTitle = title,
    postContent = content,
    postLink = link,
    postSourceId = source.id,
    postDate = date.toEpochMilliUTC(),
    postImageFileNames = gson.toJson(extractImageFileNames(state)),
    postState = state.toString()
)

fun Post.toRemote() = RemotePost(
    postId = id,
    postTitle = title,
    postContent = content,
    postLink = link,
    postSourceId = source.id,
    postDate = date.toEpochMilliUTC(),
    postImageFileNames = gson.toJson(extractImageFileNames(state))
)

fun LocalPost.toPost(getImagePath: (String) -> String) = Post(
    id = postId,
    title = postTitle,
    content = postContent,
    link = postLink,
    source = Post.PostSource.fromId(postSourceId),
    date = postDate.toLocalDateTimeUTC(),
    state = mapLocalPostState(postState, postImageFileNames, getImagePath)
)

fun RemotePost.toPost() = Post(
    id = postId,
    title = postTitle,
    content = postContent,
    link = postLink,
    source = Post.PostSource.fromId(postSourceId),
    date = postDate.toLocalDateTimeUTC(),
    state = mapRemotePostState(postImageFileNames)
)

internal fun PostReport.toRemote() = RemotePostReport(
    postId = postId,
    reporter = reporter.toRemote(),
    reason = reason
)

private fun extractImageFileNames(state: PostState): List<String> {
    return when (state) {
        is PostState.Draft -> emptyList()
        is PostState.Publishing -> state.imagePaths.mapNotNull(PostUtils.Image::extractFileNameFromPath)
        is PostState.Published -> state.imageUrls.mapNotNull(PostUtils.Image::getFileName)
        is PostState.Error -> state.imagePaths.mapNotNull(PostUtils.Image::extractFileNameFromPath)
    }
}

private fun mapLocalPostState(
    postState: String,
    postImageFileNames: String,
    getImagePath: (String) -> String
): PostState {
    val imageFileNamesType = object : TypeToken<List<String>>() {}.type
    val imageFileNames = gson.fromJson<List<String>>(postImageFileNames, imageFileNamesType)

    return when(postState) {
        PostState.Draft.TYPE -> PostState.Draft
        PostState.Publishing.TYPE -> PostState.Publishing(imageFileNames.map(getImagePath))
        PostState.Published.TYPE -> PostState.Published(imageFileNames.mapNotNull(PostUtils.Image::formatUrl))
        else -> PostState.Error(imageFileNames.map(getImagePath))
    }
}

private fun mapRemotePostState(postImageFileNames: String): PostState {
    val imageFileNamesType = object : TypeToken<List<String>>() {}.type
    val imageFileNames = gson.fromJson<List<String>>(postImageFileNames, imageFileNamesType)

    return PostState.Published(imageFileNames.mapNotNull(PostUtils.Image::formatUrl))
}