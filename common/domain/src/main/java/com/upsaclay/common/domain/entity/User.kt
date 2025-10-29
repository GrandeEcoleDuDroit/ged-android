package com.upsaclay.common.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val schoolLevel: SchoolLevel,
    val admin: Boolean = false,
    val profilePictureUrl: String? = null,
    val state: UserState = UserState.ACTIVE,
    val tester: Boolean = false
) {
    val fullName: String = "$firstName $lastName"

    enum class UserState {
        ACTIVE,
        DELETED;

        companion object {
            fun fromString(state: String): UserState =
                entries.find { it.toString() == state.lowercase() } ?: ACTIVE
        }
        override fun toString(): String = name.lowercase()
    }
}
