package com.upsaclay.common.domain.model

import com.upsaclay.common.utils.capitalizeFirstLetter

data class User(
    val id: Int = -1,
    val firstName: String,
    val lastName: String,
    val email: String,
    val schoolLevel: String,
    val isMember: Boolean = false,
    val profilePictureUrl: String? = null,
    val isOnline: Boolean = false
) {
    val fullName: String = "${firstName.capitalizeFirstLetter()} ${lastName.capitalizeFirstLetter()}"

    val shortName: String = "${firstName[0].lowercase()}${lastName.lowercase()}"

    fun isUpdated(user: User): Boolean {
        return this.id == user.id &&
                this.profilePictureUrl != user.profilePictureUrl ||
                this.isMember != user.isMember ||
                this.schoolLevel != user.schoolLevel
    }
}
