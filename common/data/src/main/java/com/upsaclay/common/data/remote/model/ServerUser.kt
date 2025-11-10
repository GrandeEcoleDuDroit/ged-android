package com.upsaclay.common.data.remote.model

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField.Server.USER_ADMIN
import com.upsaclay.common.data.UserField.Server.USER_EMAIL
import com.upsaclay.common.data.UserField.Server.USER_FIRST_NAME
import com.upsaclay.common.data.UserField.Server.USER_ID
import com.upsaclay.common.data.UserField.Server.USER_LAST_NAME
import com.upsaclay.common.data.UserField.Server.USER_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.UserField.Server.USER_SCHOOL_LEVEL
import com.upsaclay.common.data.UserField.Server.USER_STATE
import com.upsaclay.common.data.UserField.Server.USER_TESTER

data class ServerUser(
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
    val userState: String,
    @SerializedName(USER_TESTER)
    val userTester: Int = 0
)