package com.upsaclay.news.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_ID
import com.upsaclay.news.data.AnnouncementField.Local.CONTENT
import com.upsaclay.news.data.AnnouncementField.Local.DATE
import com.upsaclay.news.data.AnnouncementField.Local.STATE
import com.upsaclay.news.data.AnnouncementField.Local.TABLE_NAME
import com.upsaclay.news.data.AnnouncementField.Local.TITLE
import com.upsaclay.news.data.AnnouncementField.Local.USER_EMAIL
import com.upsaclay.news.data.AnnouncementField.Local.USER_FIRST_NAME
import com.upsaclay.news.data.AnnouncementField.Local.USER_ID
import com.upsaclay.news.data.AnnouncementField.Local.USER_STATE
import com.upsaclay.news.data.AnnouncementField.Local.USER_ADMIN
import com.upsaclay.news.data.AnnouncementField.Local.USER_LAST_NAME
import com.upsaclay.news.data.AnnouncementField.Local.USER_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.news.data.AnnouncementField.Local.USER_SCHOOL_LEVEL
import com.upsaclay.news.data.AnnouncementField.Local.USER_TESTER

@Entity(tableName = TABLE_NAME)
data class LocalAnnouncement(
    @PrimaryKey
    @ColumnInfo(name = ANNOUNCEMENT_ID)
    val announcementId: String,
    @ColumnInfo(name = TITLE)
    val announcementTitle: String?,
    @ColumnInfo(name = CONTENT)
    val announcementContent: String,
    @ColumnInfo(name = DATE)
    val announcementDate: Long,
    @ColumnInfo(name = STATE)
    val announcementState: String,
    @ColumnInfo(name = USER_ID)
    val userId: String,
    @ColumnInfo(name = USER_FIRST_NAME)
    val userFirstName: String,
    @ColumnInfo(name = USER_LAST_NAME)
    val userLastName: String,
    @ColumnInfo(name = USER_EMAIL)
    val userEmail: String,
    @ColumnInfo(name = USER_SCHOOL_LEVEL)
    val userSchoolLevel: Int,
    @ColumnInfo(name = USER_ADMIN)
    val userAdmin: Boolean,
    @ColumnInfo(name = USER_PROFILE_PICTURE_FILE_NAME)
    val userProfilePictureFileName: String?,
    @ColumnInfo(name = USER_STATE)
    val userState: String,
    @ColumnInfo(name = USER_TESTER)
    val userTester: Boolean
)