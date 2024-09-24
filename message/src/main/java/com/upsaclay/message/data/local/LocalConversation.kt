package com.upsaclay.message.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.ConvertLocalDateTimeUseCase
import com.upsaclay.common.domain.usecase.ConvertTimestampUseCase
import com.upsaclay.message.domain.model.Conversation

const val CONVERSATIONS_TABLE = "conversations_table"

@Entity(tableName = CONVERSATIONS_TABLE)
data class LocalConversation(
    @PrimaryKey
    @ColumnInfo(name = "conversation_id")
    val conversationId: String,
    @ColumnInfo(name = "interlocutor_id")
    val interlocutorId: String?,
    @ColumnInfo(name = "text")
    val text: String,
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
    val userProfilePictureUrl: String?
) {
    companion object {
        fun fromDomain(announcement: Conversation): LocalConversation {
            return LocalAnnouncement(
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
                userProfilePictureUrl = announcement.author.profilePictureUrl
            )
        }
    }

    fun toDomain() = Conversation(
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
            profilePictureUrl = userProfilePictureUrl
        )
    )
}