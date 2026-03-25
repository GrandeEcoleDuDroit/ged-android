package com.upsaclay.mission.domain.entity

import com.upsaclay.common.domain.entity.Reporter

data class MissionReport(
    val missionId: String,
    val reporter: Reporter,
    val reason: String
) {
    enum class Reason {
        SELLING_PROMOTING_INAPPROPRIATE_ITEMS,
        VIOLENT_HATEFUL_CONTENT,
        SPAM_SCAM,
        FALSE_INFORMATION,
        INTELLECTUAL_PROPERTY_VIOLATION
    }
}
