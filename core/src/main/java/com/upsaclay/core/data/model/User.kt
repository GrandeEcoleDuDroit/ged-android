package com.upsaclay.core.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val schoolLevel: String,
    val isMember: Boolean
) {
    val fullName = String.format("%s %s", firstName, lastName)
    fun toDTO(): UserDTO = UserDTO(
        userId = if(id == -1) null else id,
        userFirstName = firstName,
        userLastName = lastName,
        userEmail = email,
        userSchoolLevel = schoolLevel,
        userIsMember = if(isMember) 1 else 0
    )
}

data class UserDTO(
    @SerializedName("USER_ID")
    val userId: Int? = null,
    @SerializedName("USER_FIRST_NAME")
    val userFirstName: String,
    @SerializedName("USER_LAST_NAME")
    val userLastName: String,
    @SerializedName("USER_EMAIL")
    val userEmail: String,
    @SerializedName("USER_SCHOOL_LEVEL")
    val userSchoolLevel: String,
    @SerializedName("USER_IS_MEMBER")
    val userIsMember: Int = 0
) {
    fun toUser() = User(
        id = userId ?: 0,
        firstName = userFirstName,
        lastName = userLastName,
        email = userEmail,
        schoolLevel = userSchoolLevel,
        isMember = userIsMember == 1
    )
}

