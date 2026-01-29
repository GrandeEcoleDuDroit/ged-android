package com.upsaclay.common

import com.upsaclay.common.domain.entity.UserReport

val UserReport.Reason.stringRes: Int
    get() = when (this) {
        UserReport.Reason.HACKED_ACCOUNT -> R.string.hacked_account
        UserReport.Reason.PRETENDING_TO_BE_SOMEONE_ELSE -> R.string.pretending_to_be_someone_else
    }