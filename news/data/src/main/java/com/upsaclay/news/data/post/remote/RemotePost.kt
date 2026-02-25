package com.upsaclay.news.data.post.remote

import com.google.gson.annotations.SerializedName
import com.upsaclay.news.data.post.PostField.Remote.POST_CONTENT
import com.upsaclay.news.data.post.PostField.Remote.POST_DATE
import com.upsaclay.news.data.post.PostField.Remote.POST_ID
import com.upsaclay.news.data.post.PostField.Remote.POST_IMAGE_FILE_NAMES
import com.upsaclay.news.data.post.PostField.Remote.POST_LINK
import com.upsaclay.news.data.post.PostField.Remote.POST_SOURCE_ID
import com.upsaclay.news.data.post.PostField.Remote.POST_TITLE

data class RemotePost(
    @SerializedName(POST_ID)
    val postId: String,
    @SerializedName(POST_TITLE)
    val postTitle: String,
    @SerializedName(POST_CONTENT)
    val postContent: String?,
    @SerializedName(POST_LINK)
    val postLink: String,
    @SerializedName(POST_SOURCE_ID)
    val postSourceId: Int,
    @SerializedName(POST_DATE)
    val postDate: Long,
    @SerializedName(POST_IMAGE_FILE_NAMES)
    val postImageFileNames: String
)
