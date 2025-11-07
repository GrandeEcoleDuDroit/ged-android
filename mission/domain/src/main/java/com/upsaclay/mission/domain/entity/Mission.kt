package com.upsaclay.mission.domain.entity

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import java.time.LocalDate
import java.time.LocalDateTime

data class Mission(
    val id: Long,
    val title: String,
    val description: String,
    val schoolLevels: List<SchoolLevel>,
    val date: LocalDateTime,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val duration: String?,
    val managers: List<User>,
    val participants: List<User>,
    val maxParticipants: Int,
    val tasks: List<MissionTask>,
    val state: MissionState
) {
    val schoolLevelRestricted: Boolean
        get() = schoolLevels.isNotEmpty() &&
                schoolLevels.size < SchoolLevel.getSchoolLevels().size

    val full: Boolean
        get() = participants.size >= maxParticipants

    val expired: Boolean
        get() = endDate.isBefore(LocalDate.now())

    fun schoolLevelPermitted(schoolLevel: SchoolLevel): Boolean =
        schoolLevels.isEmpty() || schoolLevels.contains(schoolLevel)
}

sealed class MissionState {
    data class Draft(val imageUri: String? = null): MissionState() {
        override fun toString(): String = DRAFT
    }

    data class Publishing(val imagePath: String? = null): MissionState() {
        override fun toString(): String = PUBLISHING
    }

    data class Published(val imageUrl: String? = null): MissionState() {
        override fun toString(): String = PUBLISHED
    }

    data class Error(val imagePath: String? = null): MissionState() {
        override fun toString(): String = ERROR
    }

    val imageModel: String?
        get() = when (this) {
            is Draft -> imageUri
            is Publishing -> imagePath
            is Published -> imageUrl
            is Error -> imagePath
        }

    companion object {
        const val DRAFT = "DRAFT"
        const val PUBLISHING = "PUBLISHING"
        const val PUBLISHED = "PUBLISHED"
        const val ERROR = "ERROR"
    }
}
