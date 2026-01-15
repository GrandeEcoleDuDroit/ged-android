package com.upsaclay.common.data.remote.model

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.BlockedUserField.Remote.USER_ID
import com.upsaclay.common.data.BlockedUserField.Remote.BLOCKED_USER_ID
import com.upsaclay.common.data.BlockedUserField.Remote.BLOCKED_DATE

data class RemoteBlockedUser(
    @SerializedName(USER_ID)
    val userId: String,
    @SerializedName(BLOCKED_USER_ID)
    val blockedUserId: String,
    @SerializedName(BLOCKED_DATE)
    val blockedDate: Long
)