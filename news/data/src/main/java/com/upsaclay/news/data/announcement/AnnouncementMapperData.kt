package com.upsaclay.news.data.announcement

import com.upsaclay.common.data.extensions.formatUrl
import com.upsaclay.common.data.toRemote
import com.upsaclay.common.domain.UserUtils
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.User.UserState
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.news.data.announcement.local.LocalAnnouncement
import com.upsaclay.news.data.announcement.remote.InboundRemoteAnnouncement
import com.upsaclay.news.data.announcement.remote.OutbondRemoteAnnouncement
import com.upsaclay.news.data.announcement.remote.RemoteAnnouncementReport
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import com.upsaclay.news.domain.announcement.AnnouncementReport

fun Announcement.toLocal() = LocalAnnouncement(
    announcementId = id,
    announcementTitle = title,
    announcementContent = content,
    announcementDate = date.toEpochMilliUTC(),
    announcementState = state.name,
    announcementAuthorId = author.id,
    announcementAuthorFirstName = author.firstName.lowercase(),
    announcementAuthorLastName = author.lastName.lowercase(),
    announcementAuthorEmail = author.email,
    announcementAuthorSchoolLevel = author.schoolLevel.number,
    announcementAuthorAdmin = author.admin,
    announcementAuthorProfilePictureFileName = UserUtils.ProfilePicture.getFileName(author.profilePictureUrl),
    announcementAuthorState = author.state.number,
    announcementAuthorTester = author.tester
)

internal fun Announcement.toRemote() = OutbondRemoteAnnouncement(
    announcementId = id,
    announcementTitle = title,
    announcementContent = content,
    announcementDate = date.toEpochMilliUTC(),
    userId = author.id
)

fun LocalAnnouncement.toAnnouncement() = Announcement(
    id = announcementId,
    title = announcementTitle,
    content = announcementContent,
    date = announcementDate.toLocalDateTimeUTC(),
    author = User(
        id = announcementAuthorId,
        firstName = UserUtils.Name.formatName(announcementAuthorFirstName),
        lastName = UserUtils.Name.formatName(announcementAuthorLastName),
        email = announcementAuthorEmail,
        schoolLevel = SchoolLevel.fromNumber(announcementAuthorSchoolLevel),
        admin = announcementAuthorAdmin,
        profilePictureUrl = UserUtils.ProfilePicture.formatUrl(announcementAuthorProfilePictureFileName),
        state = UserState.fromNumber(announcementAuthorState),
        tester = announcementAuthorTester
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
        firstName = UserUtils.Name.formatName(userFirstName),
        lastName = UserUtils.Name.formatName(userLastName),
        email = userEmail,
        schoolLevel = SchoolLevel.fromNumber(userSchoolLevel),
        admin = userAdmin == 1,
        profilePictureUrl = UserUtils.ProfilePicture.formatUrl(userProfilePictureFileName),
        state = UserState.fromNumber(userState),
        tester = userTester == 1
    ),
    state = AnnouncementState.PUBLISHED
)

internal fun AnnouncementReport.toRemote() = RemoteAnnouncementReport(
    announcementId = announcementId,
    author = author.toRemote(),
    reporter = reporter.toRemote(),
    reason = reason
)

private fun AnnouncementReport.Author.toRemote() = RemoteAnnouncementReport.RemoteAuthor(
    fullName = fullName,
    email = email
)