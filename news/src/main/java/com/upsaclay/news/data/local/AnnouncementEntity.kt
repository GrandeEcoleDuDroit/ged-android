package com.upsaclay.news.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.ConvertLocalDateTimeUseCase
import com.upsaclay.common.domain.usecase.ConvertTimestampUseCase
import com.upsaclay.news.domain.model.Announcement

const val ANNOUNCEMENTS_LOCAL_TABLE_NAME = "announcements_local_table"

@Entity(tableName = ANNOUNCEMENTS_LOCAL_TABLE_NAME)
data class AnnouncementEntity(
    @PrimaryKey
    @ColumnInfo(name = "ANNOUNCEMENT_ID")
    val announcementId: Int,
    @ColumnInfo(name = "ANNOUNCEMENT_TITLE")
    val announcementTitle: String?,
    @ColumnInfo(name = "ANNOUNCEMENT_CONTENT")
    val announcementContent: String,
    @ColumnInfo(name = "ANNOUNCEMENT_DATE")
    val announcementDate: Long,
    @ColumnInfo("USER_ID")
    val userId: Int,
    @ColumnInfo("USER_FIRST_NAME")
    val userFirstName: String,
    @ColumnInfo("USER_LAST_NAME")
    val userLastName: String,
    @ColumnInfo("USER_EMAIL")
    val userEmail: String,
    @ColumnInfo("USER_SCHOOL_LEVEL")
    val userSchoolLevel: String,
    @ColumnInfo("USER_IS_MEMBER")
    val userIsMember: Boolean,
    @ColumnInfo("USER_PROFILE_PICTURE_URL")
    val profilePictureUrl: String?
) {

    companion object {
        fun fromAnnouncement(announcement: Announcement): AnnouncementEntity {
            return AnnouncementEntity(
                announcementId = announcement.id,
                announcementTitle = announcement.title,
                announcementContent = announcement.content,
                announcementDate = ConvertLocalDateTimeUseCase().toTimestamp(announcement.date),
                userId = announcement.author.id,
                userFirstName = announcement.author.firstName,
                userLastName = announcement.author.lastName,
                userEmail = announcement.author.email,
                userSchoolLevel = announcement.author.schoolLevel,
                userIsMember = announcement.author.isMember,
                profilePictureUrl = announcement.author.profilePictureUrl
            )
        }
    }

    fun toAnnouncement() = Announcement(
        id = announcementId,
        title = announcementTitle,
        content = announcementContent,
        date = ConvertTimestampUseCase().toLocalDateTime(announcementDate),
        author = User(
            id = userId,
            firstName = userFirstName,
            lastName = userLastName,
            email = userEmail,
            schoolLevel = userSchoolLevel,
            isMember = userIsMember,
            profilePictureUrl = profilePictureUrl
        )
    )
}