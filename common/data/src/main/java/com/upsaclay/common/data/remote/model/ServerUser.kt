package com.upsaclay.common.data.remote.model

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField

internal data class ServerUser(
    @SerializedName(UserField.Server.USER_ID)
    val userId: String,
    @SerializedName(UserField.Server.USER_FIRST_NAME)
    val userFirstName: String,
    @SerializedName(UserField.Server.USER_LAST_NAME)
    val userLastName: String,
    @SerializedName(UserField.Server.USER_EMAIL)
    val userEmail: String,
    @SerializedName(UserField.Server.USER_SCHOOL_LEVEL)
    val userSchoolLevel: Int,
    @SerializedName(UserField.Server.USER_ADMIN)
    val userAdmin: Int = 0,
    @SerializedName(UserField.Server.USER_PROFILE_PICTURE_FILE_NAME)
    val userProfilePictureFileName: String? = null,
    @SerializedName(UserField.Server.USER_STATE)
    val userState: String,
    @SerializedName(UserField.Server.USER_TESTER)
    val userTester: Int = 0
)