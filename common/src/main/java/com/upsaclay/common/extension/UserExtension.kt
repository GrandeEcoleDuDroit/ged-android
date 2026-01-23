package com.upsaclay.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User

@Composable
fun User.displayName(): String {
    return if (state == User.UserState.DELETED) {
        stringResource(R.string.deleted_user)
    } else {
        fullName
    }
}