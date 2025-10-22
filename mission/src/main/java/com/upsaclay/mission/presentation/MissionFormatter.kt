package com.upsaclay.mission.presentation

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.utils.LocalDateFormatter
import java.time.LocalDate

object MissionFormatter {
    fun formatSchoolLevels(schoolLevels: List<SchoolLevel>, emptyText: String): String {
        return schoolLevels
            .takeUnless { it.isEmpty() || it.size == SchoolLevel.entries.size }
            ?.sortedBy { it.ordinal }
            ?.joinToString(separator = " - ")
            ?: emptyText
    }

    fun formatDate(startDate: LocalDate, endDate: LocalDate): String {
        return when {
            startDate == endDate -> LocalDateFormatter.formatDayMonth(startDate)
            else -> LocalDateFormatter.formatDayMonth(startDate) + " - " +
                    LocalDateFormatter.formatDayMonth(endDate)
        }
    }

    fun formatRemainingParticipants(participantsCount: Int, maxParticipants: Int): String {
        val remaining = maxParticipants - participantsCount
        return remaining.takeIf { it < 100 }?.toString() ?: "99+"
    }
}