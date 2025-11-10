package com.upsaclay.mission.presentation

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.utils.LocalDateFormatter
import java.time.LocalDate
import kotlin.math.max

object MissionFormatter {
    fun formatSchoolLevels(schoolLevels: List<SchoolLevel>): String =
        schoolLevels.sortedBy { it.ordinal }.joinToString(separator = " - ")

    fun formatDate(startDate: LocalDate, endDate: LocalDate): String {
        return when {
            startDate == endDate -> LocalDateFormatter.formatDayMonthYear(startDate)
            else -> LocalDateFormatter.formatDayMonthYear(startDate) + " - " +
                    LocalDateFormatter.formatDayMonthYear(endDate)
        }
    }

    fun formatRemainingParticipants(participantsCount: Int, maxParticipants: Int): String {
        val remaining = max(maxParticipants - participantsCount, 0)
        return remaining.takeIf { it < 100 }?.toString() ?: "99+"
    }
}