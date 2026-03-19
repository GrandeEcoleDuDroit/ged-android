package com.upsaclay.common.presentation.user

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2

class UserPreviewParameterProvider: PreviewParameterProvider<UserPreviewParameterData> {
    override val values = sequenceOf(UserPreviewParameterData(userFixture, userFixture2))
}

data class UserPreviewParameterData(
    val user: User,
    val currentUser: User,
)