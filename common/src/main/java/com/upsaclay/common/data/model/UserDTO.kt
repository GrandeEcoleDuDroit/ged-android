package com.upsaclay.common.data.model

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.domain.model.User

internal data class UserDTO(
    @SerializedName("USER_ID") val userId: Int? = null,
    @SerializedName("USER_FIRST_NAME") val userFirstName: String,
    @SerializedName("USER_LAST_NAME") val userLastName: String,
    @SerializedName("USER_EMAIL") val userEmail: String,
    @SerializedName("USER_SCHOOL_LEVEL") val userSchoolLevel: String,
    @SerializedName("USER_IS_MEMBER") val userIsMember: Int = 0,
    @SerializedName("USER_PROFILE_PICTURE_URL") val userProfilePictureUrl: String? = null
) {
    companion object {
        fun fromDomain(user: User) = UserDTO(
            userId = if (user.id == -1) null else user.id,
            userFirstName = user.firstName,
            userLastName = user.lastName,
            userEmail = user.email,
            userSchoolLevel = user.schoolLevel,
            userIsMember = if (user.isMember) 1 else 0,
            userProfilePictureUrl = user.profilePictureUrl
        )
    }

    fun toDomain() = User(
        id = this.userId ?: -1,
        firstName = this.userFirstName,
        lastName = this.userLastName,
        email = this.userEmail,
        schoolLevel = this.userSchoolLevel,
        isMember = this.userIsMember == 1,
        profilePictureUrl = this.userProfilePictureUrl
    )
}


