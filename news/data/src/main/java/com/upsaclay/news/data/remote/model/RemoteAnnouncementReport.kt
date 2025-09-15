package com.upsaclay.news.data.remote.model

data class RemoteAnnouncementReport(
    val announcementId: String,
    val authorInfo: RemoteAnnouncementReport.RemoteUserInfo,
    val userInfo: RemoteAnnouncementReport.RemoteUserInfo,
    val reason: String
) {
    data class RemoteUserInfo(
        val fullName: String,
        val email: String
    )
}