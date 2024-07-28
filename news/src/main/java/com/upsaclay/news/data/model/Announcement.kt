package com.upsaclay.news.data.model

import com.google.gson.annotations.SerializedName
import com.upsaclay.core.domain.model.User
import java.time.LocalDateTime

data class Announcement(
    val id: Int,
    val title: String,
    val content: String,
    val date: LocalDateTime,
    val author: User
) {
    fun toDTO() = AnnouncementDTO(
        announcementId = id,
        announcementTitle = title,
        announcementContent = content,
        announcementDate = date.toString(),
        userId = author.id
    )
}

data class AnnouncementWithUserDTO(
    @SerializedName("ANNOUNCEMENT_ID")
    val announcementId: Int,
    @SerializedName("ANNOUNCEMENT_TITLE")
    val announcementTitle: String,
    @SerializedName("ANNOUNCEMENT_CONTENT")
    val announcementContent: String,
    @SerializedName("ANNOUNCEMENT_DATE")
    val announcementDate: String,
    @SerializedName("USER_ID")
    val userId: Int,
    @SerializedName("USER_FIRST_NAME")
    val userFirstName: String,
    @SerializedName("USER_LAST_NAME")
    val userLastName: String,
    @SerializedName("USER_EMAIL")
    val userEmail: String,
    @SerializedName("USER_SCHOOL_LEVEL")
    val userSchoolLevel: String,
    @SerializedName("USER_IS_MEMBER")
    val userIsMember: Int
) {
    fun toAnnouncement() = Announcement(
        id = announcementId,
        title = announcementTitle,
        content = announcementContent,
        date = LocalDateTime.parse(announcementDate),
        author = User(
            id = userId,
            firstName = userFirstName,
            lastName = userLastName,
            email = userEmail,
            schoolLevel = userSchoolLevel,
            isMember = userIsMember == 1
        )
    )
}

data class AnnouncementDTO(
    @SerializedName("ANNOUNCEMENT_ID")
    val announcementId: Int,
    @SerializedName("ANNOUNCEMENT_TITLE")
    val announcementTitle: String,
    @SerializedName("ANNOUNCEMENT_CONTENT")
    val announcementContent: String,
    @SerializedName("ANNOUNCEMENT_DATE")
    val announcementDate: String,
    @SerializedName("USER_ID")
    val userId: Int,
)