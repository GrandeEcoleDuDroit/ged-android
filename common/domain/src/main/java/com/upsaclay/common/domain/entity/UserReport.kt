package com.upsaclay.common.domain.entity

data class UserReport(
    val reportedUser: ReportedUser,
    val reporter: Reporter,
    val reason: Reason
) {
    data class ReportedUser(
        val id: String,
        val fullName: String,
        val email: String
    )

    data class Reporter(
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