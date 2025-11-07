package com.upsaclay.common.data

import androidx.core.graphics.scaleMatrix
import com.upsaclay.common.data.UrlUtils.extractFileName
import com.upsaclay.common.data.UrlUtils.formatOracleBucketUrl
import com.upsaclay.common.data.local.LocalUser
import com.upsaclay.common.data.remote.model.FirestoreUser
import com.upsaclay.common.data.remote.model.RemoteUserReport
import com.upsaclay.common.data.remote.model.ServerUser
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.extensions.uppercaseFirstLetter

internal fun User.toLocal() = LocalUser(
    userId = id,
    firstName = firstName.lowercase(),
    lastName = lastName.lowercase(),
    email = email,
    schoolLevel = schoolLevel.number,
    admin = if (admin) 1 else 0,
    profilePictureFileName = extractFileName(profilePictureUrl),
    state = state.toString(),
    tester = if (tester) 1 else 0
)

internal fun User.toServerUser() = ServerUser(
    userId = id,
    userFirstName = firstName.lowercase(),
    userLastName = lastName.lowercase(),
    userEmail = email,
    userSchoolLevel = schoolLevel.number,
    userAdmin = if (admin) 1 else 0,
    userProfilePictureFileName = extractFileName(profilePictureUrl),
    userState = state.toString(),
    userTester = if (tester) 1 else 0
)

internal fun User.toFirestoreUser() = FirestoreUser(
    userId = id,
    firstName = firstName.lowercase(),
    lastName = lastName.lowercase(),
    email = email,
    schoolLevel = schoolLevel.number,
    admin = admin,
    profilePictureFileName = extractFileName(profilePictureUrl),
    state = state.toString(),
    tester = tester
)

internal fun LocalUser.toUser() = User(
    id = userId,
    firstName = firstName.uppercaseFirstLetter(),
    lastName = lastName.uppercaseFirstLetter(),
    email = email,
    schoolLevel = SchoolLevel.fromNumber(schoolLevel),
    admin = admin == 1,
    profilePictureUrl = formatOracleBucketUrl(profilePictureFileName),
    state = User.UserState.fromString(state),
    tester = tester == 1
)

internal fun FirestoreUser.toUser() = User(
    id = userId,
    firstName = firstName.uppercaseFirstLetter(),
    lastName = lastName.uppercaseFirstLetter(),
    email = email,
    schoolLevel = SchoolLevel.fromNumber(schoolLevel),
    admin = admin,
    profilePictureUrl = formatOracleBucketUrl(profilePictureFileName),
    state = User.UserState.fromString(state),
    tester = tester
)

fun ServerUser.toUser() = User(
    id = userId,
    firstName = userFirstName,
    lastName = userLastName,
    email = userEmail,
    schoolLevel = SchoolLevel.fromNumber(userSchoolLevel),
    admin = userAdmin == 1,
    profilePictureUrl = formatOracleBucketUrl(userProfilePictureFileName),
    state = User.UserState.fromString(userState),
    tester = userTester == 1
)

internal fun UserReport.toRemote() = RemoteUserReport(
    userId = userId,
    userInfo = userInfo.toRemote(),
    reporterInfo = reporterInfo.toRemote(),
    reason = reason.toString()
)

internal fun UserReport.UserInfo.toRemote() = RemoteUserReport.RemoteUserInfo(
    fullName = fullName,
    email = email
)
