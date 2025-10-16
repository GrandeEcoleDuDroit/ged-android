package com.upsaclay.common.data.remote.model

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField

internal data class ServerUser(
    @SerializedName(UserField.Oracle.USER_ID)
    val userId: String,
    @SerializedName(UserField.Oracle.USER_FIRST_NAME)
    val userFirstName: String,
    @SerializedName(UserField.Oracle.USER_LAST_NAME)
    val userLastName: String,
    @SerializedName(UserField.Oracle.USER_EMAIL)
    val userEmail: String,
    @SerializedName(UserField.Oracle.USER_SCHOOL_LEVEL)
    val userSchoolLevel: String,
    @SerializedName(UserField.Oracle.USER_IS_MEMBER)
    val userIsMember: Int = 0,
    @SerializedName(UserField.Oracle.USER_PROFILE_PICTURE_FILE_NAME)
    val userProfilePictureFileName: String? = null,
    @SerializedName(UserField.Oracle.USER_IS_DELETED)
    val userIsDeleted: Int = 0
)