package com.upsaclay.common.domain.entity

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

    enum class UserState(val number: Int) {
        ACTIVE(1),
        DELETED(2);

        companion object {
            fun fromNumber(number: Int): UserState = entries.find { it.number == number } ?: ACTIVE
        }
    }
}
