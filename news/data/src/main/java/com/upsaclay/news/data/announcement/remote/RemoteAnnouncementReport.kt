package com.upsaclay.news.data.announcement.remote

import com.upsaclay.common.data.remote.model.RemoteReporter

data class RemoteAnnouncementReport(
    val announcementId: String,
    val author: RemoteAuthor,
    val reporter: RemoteReporter,
    val reason: String
) {
    data class RemoteAuthor(
        val fullName: String,
        val email: String
    )
}