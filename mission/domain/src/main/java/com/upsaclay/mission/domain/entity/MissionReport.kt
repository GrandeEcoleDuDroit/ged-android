package com.upsaclay.mission.domain.entity

data class MissionReport(
    val missionId: String,
    val reporter: Reporter,
    val reason: String
) {
    data class Reporter(
        val fullName: String,
        val email: String
    )

    enum class Reason {
        SELLING_PROMOTING_INAPPROPRIATE_ITEMS,
        VIOLENT_HATEFUL_CONTENT,
        SPAM_SCAM,
        FALSE_INFORMATION,
        INTELLECTUAL_PROPERTY_VIOLATION
    }
}
