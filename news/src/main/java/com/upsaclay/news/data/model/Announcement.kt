package com.upsaclay.news.data.model

import com.google.gson.annotations.SerializedName
import com.upsaclay.core.data.User
import java.time.LocalDateTime

data class Announcement(
    val id: Int,
    val title: String,
    val content: String,
    val date: LocalDateTime,
    val author: User
)

data class AnnouncementWithUserDTO(
    @SerializedName("announcement_id")
    val announcementId: Int,
    @SerializedName("announcement_title")
    val announcementTitle: String,
    @SerializedName("announcement_content")
    val announcementContent: String,
    @SerializedName("announcement_date")
    val announcementDate: LocalDateTime,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_first_name")
    val userFirstName: String,
    @SerializedName("user_last_name")
    val userLastName: String,
    @SerializedName("user_email")
    val userEmail: String
) {
    fun toDomain() = Announcement(
        id = announcementId,
        title = announcementTitle,
        content = announcementContent,
        date = announcementDate,
        author = User(
            id = userId,
            firstName = userFirstName,
            lastName = userLastName,
            email = userEmail
        )
    )
}

data class AnnouncementDTO(
    @SerializedName("announcement_id")
    val announcementId: Int,
    @SerializedName("announcement_title")
    val announcementTitle: String,
    @SerializedName("announcement_content")
    val announcementContent: String,
    @SerializedName("announcement_date")
    val announcementDate: LocalDateTime,
    @SerializedName("user_id")
    val userId: Int,
) {
    companion object {
        fun fromDomain(announcement: Announcement) = AnnouncementDTO(
            announcementId = announcement.id,
            announcementTitle = announcement.title,
            announcementContent = announcement.content,
            announcementDate = announcement.date,
            userId = announcement.author.id
        )
    }
}