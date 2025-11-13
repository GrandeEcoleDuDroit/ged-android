package com.upsaclay.common.data.local

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField

data class LocalUser(
    @SerializedName(UserField.Local.USER_ID)
    val userId: String,
    @SerializedName(UserField.Local.FIRST_NAME)
    val firstName: String,
    @SerializedName(UserField.Local.LAST_NAME)
    val lastName: String,
    @SerializedName(UserField.Local.EMAIL)
    val email: String,
    @SerializedName(UserField.Local.SCHOOL_LEVEL)
    val schoolLevel: Int,
    @SerializedName(UserField.Local.ADMIN)
    val admin: Int = 0,
    @SerializedName(UserField.Local.PROFILE_PICTURE_FILE_NAME)
    val profilePictureFileName: String? = null,
    @SerializedName(UserField.Local.STATE)
    val state: String,
    @SerializedName(UserField.Local.TESTER)
    val tester: Int = 0
)