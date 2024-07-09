package com.upsaclay.core.data

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String
) {
    val fullname = String.format("%s %s", firstName, lastName)
}

data class UserDTO(
    @SerializedName("USER_ID")
    val userId: Int,
    @SerializedName("USER_FIRST_NAME")
    val userFirstName: String,
    @SerializedName("USER_LAST_NAME")
    val userLastName: String,
    @SerializedName("USER_EMAIL")
    val userEmail: String
)
