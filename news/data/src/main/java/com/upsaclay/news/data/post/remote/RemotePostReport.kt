package com.upsaclay.news.data.post.remote

import com.upsaclay.common.data.remote.model.RemoteReporter

data class RemotePostReport(
    val postId: String,
    val reporter: RemoteReporter,
    val reason: String
)
