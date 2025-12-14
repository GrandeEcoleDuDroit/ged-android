package com.upsaclay.news.data.remote.model

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

    data class RemoteReporter(
        val fullName: String,
        val email: String
    )
}