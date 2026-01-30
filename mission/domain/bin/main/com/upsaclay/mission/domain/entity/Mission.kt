package com.upsaclay.mission.domain.entity

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

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
                schoolLevels.size < SchoolLevel.all.size

    val full: Boolean
        get() = participants.size >= maxParticipants

    val completed: Boolean
        get() = endDate.isBefore(LocalDate.now(ZoneOffset.UTC))

    sealed class MissionState {
        data object Draft: MissionState() {
            override fun toString(): String = TYPE
            const val TYPE = "DRAFT"
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
                is Draft -> null
                is Publishing -> imagePath
                is Published -> imageUrl
                is Error -> imagePath
            }
    }
}


