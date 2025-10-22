package com.upsaclay.news.data

import com.upsaclay.common.data.UrlUtils.formatOracleBucketUrl
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.news.data.local.model.LocalAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncementReport
import com.upsaclay.news.data.remote.model.RemoteAnnouncementWithUser
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import com.upsaclay.news.domain.entity.AnnouncementState

fun Announcement.toLocal() = LocalAnnouncement(
    announcementId = id,
    announcementTitle = title,
    announcementContent = content,
    announcementDate = date.toEpochMilliUTC(),
    announcementState = state.name,
    userId = author.id,
    userFirstName = author.firstName,
    userLastName = author.lastName,
    userEmail = author.email,
    userSchoolLevel = author.schoolLevel,
    userIsMember = author.isMember,
    userProfilePictureFileName = author.profilePictureUrl,
    userIsDeleted = author.isDeleted
)

fun LocalAnnouncement.toAnnouncement() = Announcement(
    id = announcementId,
    title = announcementTitle,
    content = announcementContent,
    date = announcementDate.toLocalDateTimeUTC(),
    author = User(
        id = userId,
        firstName = userFirstName,
        lastName = userLastName,
        email = userEmail,
        schoolLevel = userSchoolLevel,
        isMember = userIsMember,
        profilePictureUrl = userProfilePictureFileName,
        isDeleted = userIsDeleted
    ),
    state = AnnouncementState.valueOf(announcementState)
)

internal fun RemoteAnnouncementWithUser.toAnnouncement() = Announcement(
    id = announcementId,
    title = announcementTitle,
    content = announcementContent,
    date = announcementDate.toLocalDateTimeUTC(),
    author = User(
        id = userId,
        firstName = userFirstName,
        lastName = userLastName,
        email = userEmail,
        schoolLevel = userSchoolLevel,
        isMember = userIsMember == 1,
        profilePictureUrl = formatOracleBucketUrl(userProfilePictureFileName),
        isDeleted = userIsDeleted == 1
    ),
    state = AnnouncementState.PUBLISHED
)

internal fun Announcement.toRemote() = RemoteAnnouncement(
    announcementId = id,
    announcementTitle = title,
    announcementContent = content,
    announcementDate = date.toEpochMilliUTC(),
    userId = author.id
)

internal fun AnnouncementReport.toRemote() = RemoteAnnouncementReport(
    announcementId = announcementId,
    authorInfo = authorInfo.toRemote(),
    userInfo = userInfo.toRemote(),
    reason = reason.toString()
)

internal fun AnnouncementReport.UserInfo.toRemote() = RemoteAnnouncementReport.RemoteUserInfo(
    fullName = fullName,
    email = email
)
