package com.upsaclay.common.domain.model

data class User(
    val id: Int = -1,
    val firstName: String,
    val lastName: String,
    val email: String,
    val schoolLevel: String,
    val isMember: Boolean = false,
    val profilePictureUrl: String? = null
) {
    val fullName = String.format("%s %s", firstName, lastName)

    override fun equals(other: Any?): Boolean {
        return other is User && other.id == id &&
                other.profilePictureUrl == profilePictureUrl
    }
}
