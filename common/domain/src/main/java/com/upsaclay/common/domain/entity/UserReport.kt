package com.upsaclay.common.domain.entity

data class UserReport(
    val reportedUser: ReportedUser,
    val reporter: Reporter,
    val reason: String
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
        PRETENDING_TO_BE_SOMEONE_ELSE
    }
}