package com.upsaclay.news.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_ADMIN
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_EMAIL
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_FIRST_NAME
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_ID
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_LAST_NAME
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_SCHOOL_LEVEL
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_STATE
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_TESTER
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_CONTENT
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_DATE
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_ID
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_STATE
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_TABLE_NAME
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_TITLE

@Entity(tableName = ANNOUNCEMENT_TABLE_NAME)
data class LocalAnnouncement(
    @PrimaryKey
    @ColumnInfo(name = ANNOUNCEMENT_ID)
    val announcementId: String,
    @ColumnInfo(name = ANNOUNCEMENT_TITLE)
    val announcementTitle: String?,
    @ColumnInfo(name = ANNOUNCEMENT_CONTENT)
    val announcementContent: String,
    @ColumnInfo(name = ANNOUNCEMENT_DATE)
    val announcementDate: Long,
    @ColumnInfo(name = ANNOUNCEMENT_STATE)
    val announcementState: String,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_ID)
    val announcementAuthorId: String,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_FIRST_NAME)
    val announcementAuthorFirstName: String,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_LAST_NAME)
    val announcementAuthorLastName: String,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_EMAIL)
    val announcementAuthorEmail: String,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_SCHOOL_LEVEL)
    val announcementAuthorSchoolLevel: Int,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_ADMIN)
    val announcementAuthorAdmin: Boolean,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_PROFILE_PICTURE_FILE_NAME)
    val announcementAuthorProfilePictureFileName: String?,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_STATE)
    val announcementAuthorState: String,
    @ColumnInfo(name = ANNOUNCEMENT_AUTHOR_TESTER)
    val announcementAuthorTester: Boolean
)