package com.upsaclay.common.data.local

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField.Local.USER_ADMIN
import com.upsaclay.common.data.UserField.Local.USER_EMAIL
import com.upsaclay.common.data.UserField.Local.USER_FIRST_NAME
import com.upsaclay.common.data.UserField.Local.USER_ID
import com.upsaclay.common.data.UserField.Local.USER_LAST_NAME
import com.upsaclay.common.data.UserField.Local.USER_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.UserField.Local.USER_SCHOOL_LEVEL
import com.upsaclay.common.data.UserField.Local.USER_STATE
import com.upsaclay.common.data.UserField.Local.USER_TESTER

data class LocalUser(
    @SerializedName(USER_ID)
    val userId: String,
    @SerializedName(USER_FIRST_NAME)
    val userFirstName: String,
    @SerializedName(USER_LAST_NAME)
    val userLastName: String,
    @SerializedName(USER_EMAIL)
    val userEmail: String,
    @SerializedName(USER_SCHOOL_LEVEL)
    val userSchoolLevel: Int,
    @SerializedName(USER_ADMIN)
    val userAdmin: Int = 0,
    @SerializedName(USER_PROFILE_PICTURE_FILE_NAME)
    val userProfilePictureFileName: String? = null,
    @SerializedName(USER_STATE)
    val userState: Int,
    @SerializedName(USER_TESTER)
    val userTester: Int = 0
)