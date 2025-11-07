package com.upsaclay.mission.domain.entity

data class MissionReport(
    val missionId: Long,
    val authorInfo: MissionReport.UserInfo,
    val reason: MissionReport.Reason,
) {
    data class UserInfo(
        val fullName: String,
        val email: String
    )

    enum class Reason {
        SELLING_PROMOTING_INAPPROPRIATE_ITEMS,
        VIOLENT_HATEFUL_CONTENT,
        SPAM_SCAM,
        FALSE_INFORMATION,
        INTELLECTUAL_PROPERTY_VIOLATION,
        OTHER;

        override fun toString(): String {
            return when (this) {
                SELLING_PROMOTING_INAPPROPRIATE_ITEMS -> "Selling or promoting inappropriate items"
                VIOLENT_HATEFUL_CONTENT -> "Violent or hateful content"
                SPAM_SCAM -> "Spam or scam"
                FALSE_INFORMATION -> "False information"
                INTELLECTUAL_PROPERTY_VIOLATION -> "Intellectual property violation"
                OTHER -> "Other"
            }
        }
    }
}
