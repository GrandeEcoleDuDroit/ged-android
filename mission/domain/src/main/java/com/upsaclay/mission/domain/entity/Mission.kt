package com.upsaclay.mission.domain.entity

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import java.time.LocalDate
import java.time.LocalDateTime

data class Mission(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDateTime,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val schoolLevels: List<SchoolLevel>,
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

    val complete: Boolean
        get() = endDate.isBefore(LocalDate.now())

    fun schoolLevelPermitted(schoolLevel: SchoolLevel): Boolean =
        schoolLevels.isEmpty() || schoolLevels.contains(schoolLevel)
}

sealed class MissionState {
    data class Draft(val imageUri: String? = null): MissionState() {
        override fun toString(): String = TYPE
        companion object {
            const val TYPE = "DRAFT"
        }
    }

    data class Publishing(val imagePath: String? = null): MissionState() {
        override fun toString(): String = TYPE
        companion object {
            const val TYPE = "PUBLISHING"
        }
    }

    data class Published(val imageUrl: String? = null): MissionState() {
        override fun toString(): String = TYPE
        companion object {
            const val TYPE = "PUBLISHED"
        }
    }

    data class Error(val imagePath: String? = null): MissionState() {
        override fun toString(): String = TYPE
        companion object {
            const val TYPE = "ERROR"
        }
    }

    val imageReference: String?
        get() = when (this) {
            is Draft -> imageUri
            is Publishing -> imagePath
            is Published -> imageUrl
            is Error -> imagePath
        }
}
