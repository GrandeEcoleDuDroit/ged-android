package com.upsaclay.common.domain.entity

data class UserReport(
    val userId: String,
    val userInfo: UserInfo,
    val reporterInfo: UserInfo,
    val reason: Reason
) {
    data class UserInfo(
        val fullName: String,
        val email: String
    )

    enum class Reason {
        HACKED_ACCOUNT,
        PRETENDING_TO_BE_SOMEONE_ELSE,
        OTHER;

        override fun toString(): String {
            return when (this) {
                HACKED_ACCOUNT -> "Hacked account"
                PRETENDING_TO_BE_SOMEONE_ELSE -> "Pretending to be someone else"
                OTHER -> "Other"
            }
        }
    }
}