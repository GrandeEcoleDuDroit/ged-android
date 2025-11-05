package com.upsaclay.mission.domain.entity

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import java.time.LocalDate
import java.time.LocalDateTime

data class Mission(
    val id: Int,
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
        get() = schoolLevels.isNotEmpty() && schoolLevels.size < SchoolLevel.entries.size

    val full: Boolean
        get() = participants.size >= maxParticipants

    val expired: Boolean
        get() = endDate.isBefore(LocalDate.now())

    fun schoolLevelPermitted(schoolLevel: SchoolLevel): Boolean =
        schoolLevels.isEmpty() || schoolLevels.contains(schoolLevel)
}

sealed class MissionState {
    data class Draft(val imageUri: String? = null): MissionState()
    data class Publishing(val imagePath: String? = null): MissionState()
    data class Published(val imageUrl: String? = null): MissionState()
    data class Error(val imagePath: String? = null): MissionState()

    override fun toString(): String {
        return when (this) {
            is Draft -> "DRAFT"
            is Publishing -> "PUBLISHING"
            is Published -> "PUBLISHED"
            is Error -> "ERROR"
        }
    }

    companion object {
        fun fromString(value: String, imagePathOrUri: String?): MissionState {
            return when (value) {
                "DRAFT" -> Draft(imageUri = imagePathOrUri)
                "PUBLISHING" -> Publishing(imagePath = imagePathOrUri)
                "PUBLISHED" -> Published(imageUrl = imagePathOrUri)
                "ERROR" -> Error(imagePath = imagePathOrUri)
                else -> Error(imagePath = imagePathOrUri)
            }
        }
    }
}
