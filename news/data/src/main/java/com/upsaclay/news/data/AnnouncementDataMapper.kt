package com.upsaclay.news.data

import com.upsaclay.common.data.UrlUtils.formatOracleBucketUrl
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.User.UserState
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.common.domain.extensions.uppercaseFirstLetter
import com.upsaclay.news.data.local.model.LocalAnnouncement
import com.upsaclay.news.data.remote.model.InboundRemoteAnnouncement
import com.upsaclay.news.data.remote.model.OutbondRemoteAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncementReport
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.entity.AnnouncementReport

fun Announcement.toLocal() = LocalAnnouncement(
    announcementId = id,
    announcementTitle = title,
    announcementContent = content,
    announcementDate = date.toEpochMilliUTC(),
    announcementState = state.name,
    userId = author.id,
    userFirstName = author.firstName.lowercase(),
    userLastName = author.lastName.lowercase(),
    userEmail = author.email,
    userSchoolLevel = author.schoolLevel.number,
    userAdmin = author.admin,
    userProfilePictureFileName = author.profilePictureUrl,
    userState = author.state.toString(),
    userTester = author.tester
)

fun LocalAnnouncement.toAnnouncement() = Announcement(
    id = announcementId,
    title = announcementTitle,
    content = announcementContent,
    date = announcementDate.toLocalDateTimeUTC(),
    author = User(
        id = userId,
        firstName = userFirstName.uppercaseFirstLetter(),
        lastName = userLastName.uppercaseFirstLetter(),
        email = userEmail,
        schoolLevel = SchoolLevel.fromNumber(userSchoolLevel),
        admin = userAdmin,
        profilePictureUrl = userProfilePictureFileName,
        state = UserState.fromString(userState),
        tester = userTester
    ),
    state = AnnouncementState.valueOf(announcementState)
)

internal fun InboundRemoteAnnouncement.toAnnouncement() = Announcement(
    id = announcementId,
    title = announcementTitle,
    content = announcementContent,
    date = announcementDate.toLocalDateTimeUTC(),
    author = User(
        id = userId,
        firstName = userFirstName.uppercaseFirstLetter(),
        lastName = userLastName.uppercaseFirstLetter(),
        email = userEmail,
        schoolLevel = SchoolLevel.fromNumber(userSchoolLevel),
        admin = userAdmin == 1,
        profilePictureUrl = formatOracleBucketUrl(userProfilePictureFileName),
        state = UserState.fromString(userState),
        tester = userTester == 1
    ),
    state = AnnouncementState.PUBLISHED
)

internal fun Announcement.toRemote() = OutbondRemoteAnnouncement(
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
