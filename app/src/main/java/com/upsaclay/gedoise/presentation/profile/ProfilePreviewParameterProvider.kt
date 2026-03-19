package com.upsaclay.gedoise.presentation.profile

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture

class BlockedUsersPreviewParameterProvider: PreviewParameterProvider<List<User>> {
    override val values = sequenceOf(usersFixture)
}

class AccountInformationPreviewParameterProvider: PreviewParameterProvider<User> {
    override val values = sequenceOf(userFixture)
}