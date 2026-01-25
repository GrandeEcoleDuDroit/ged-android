package com.upsaclay.common.data

import com.upsaclay.common.data.extensions.formatUrl
import com.upsaclay.common.data.local.LocalUser
import com.upsaclay.common.data.remote.model.FirestoreUser
import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.common.data.remote.model.RemoteBlockedUser
import com.upsaclay.common.data.remote.model.RemoteUserReport
import com.upsaclay.common.domain.UserUtils
import com.upsaclay.common.domain.entity.BlockedUser
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC

fun User.toLocal() = LocalUser(
    userId = id,
    userFirstName = firstName.lowercase(),
    userLastName = lastName.lowercase(),
    userEmail = email,
    userSchoolLevel = schoolLevel.number,
    userAdmin = if (admin) 1 else 0,
    userProfilePictureFileName = UserUtils.ProfilePicture.getFileName(profilePictureUrl),
    userState = state.number,
    userTester = if (tester) 1 else 0
)

fun User.toOracleUser() = OracleUser(
    userId = id,
    userFirstName = firstName.lowercase(),
    userLastName = lastName.lowercase(),
    userEmail = email,
    userSchoolLevel = schoolLevel.number,
    userAdmin = if (admin) 1 else 0,
    userProfilePictureFileName = UserUtils.ProfilePicture.getFileName(profilePictureUrl),
    userState = state.number,
    userTester = if (tester) 1 else 0
)

internal fun LocalUser.toUser() = User(
    id = userId,
    firstName = UserUtils.Name.formatName(userFirstName),
    lastName = UserUtils.Name.formatName(userLastName),
    email = userEmail,
    schoolLevel = SchoolLevel.fromNumber(userSchoolLevel),
    admin = userAdmin == 1,
    profilePictureUrl = UserUtils.ProfilePicture.formatUrl(userProfilePictureFileName),
    state = User.UserState.fromNumber(userState),
    tester = userTester == 1
)

internal fun FirestoreUser.toUser() = User(
    id = userId,
    firstName = UserUtils.Name.formatName(firstName),
    lastName = UserUtils.Name.formatName(lastName),
    email = email,
    schoolLevel = SchoolLevel.fromNumber(schoolLevel),
    admin = admin,
    profilePictureUrl = UserUtils.ProfilePicture.formatUrl(profilePictureFileName),
    state = User.UserState.fromNumber(state),
    tester = tester
)

fun OracleUser.toUser() = User(
    id = userId,
    firstName = UserUtils.Name.formatName(userFirstName),
    lastName = UserUtils.Name.formatName(userLastName),
    email = userEmail,
    schoolLevel = SchoolLevel.fromNumber(userSchoolLevel),
    admin = userAdmin == 1,
    profilePictureUrl = UserUtils.ProfilePicture.formatUrl(userProfilePictureFileName),
    state = User.UserState.fromNumber(userState),
    tester = userTester == 1
)

internal fun UserReport.toRemote() = RemoteUserReport(
    user = reportedUser.toRemote(),
    reporter = reporter.toRemote(),
    reason = reason.toString()
)

private fun UserReport.ReportedUser.toRemote() = RemoteUserReport.RemoteReportedUser(
    id = id,
    fullName = fullName,
    email = email
)

private fun UserReport.Reporter.toRemote() = RemoteUserReport.RemoteReporter(
    fullName = fullName,
    email = email
)

internal fun BlockedUser.toRemote(currentUserId: String) = RemoteBlockedUser(
    userId = currentUserId,
    blockedUserId = userId,
    blockedDate = date.toEpochMilliUTC()
)

internal fun RemoteBlockedUser.toBlockedUser() = BlockedUser(
    userId = userId,
    date = blockedDate.toLocalDateTimeUTC()
)