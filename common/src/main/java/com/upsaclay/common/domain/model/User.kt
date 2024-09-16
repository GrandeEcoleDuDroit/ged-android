package com.upsaclay.common.domain.model

import com.upsaclay.common.utils.capitalizeFirstLetter

data class User(
    val id: Int = -1,
    val firstName: String,
    val lastName: String,
    val email: String,
    val schoolLevel: String,
    val isMember: Boolean = false,
    val profilePictureUrl: String? = null
) {
    val fullName: String = "${firstName.capitalizeFirstLetter()} ${lastName.capitalizeFirstLetter()}"

    override fun equals(other: Any?): Boolean {
        return other is User && other.id == id &&
                other.profilePictureUrl == profilePictureUrl &&
                other.schoolLevel == schoolLevel &&
                other.isMember == isMember
    }
}
