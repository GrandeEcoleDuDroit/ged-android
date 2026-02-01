package com.upsaclay.app.domain

import com.upsaclay.app.domain.entity.NotificationPreferences
import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.userFixture

val fcmTokenFixture = FcmToken(userFixture.id, true)

val notificationPreferencesFixture = NotificationPreferences(notificationAllowed = false)