package com.upsaclay.mission.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.utils.DateUtils
import java.time.LocalDate
import kotlin.math.max

object MissionPresentationUtils {
    const val MAX_TITLE_LENGTH = 100
    const val MAX_DESCRIPTION_LENGTH = 1000
    const val MAX_DURATION_LENGTH = 200
    const val MAX_TASK_LENGTH = 300

    val titleStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val contentStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyLarge

    fun formatSchoolLevels(schoolLevels: List<SchoolLevel>): String =
        schoolLevels.sortedBy { it.ordinal }.joinToString(separator = " - ")

    fun formatDate(startDate: LocalDate, endDate: LocalDate): String {
        return if(startDate == endDate) {
            DateUtils.formatDayMonthYear(startDate)
        } else {
            DateUtils.formatDayMonthYear(startDate) + " - " +
                    DateUtils.formatDayMonthYear(endDate)
        }
    }

    fun formatRemainingParticipants(participantsCount: Int, maxParticipants: Int): String {
        val remaining = max(maxParticipants - participantsCount, 0)
        return remaining.takeIf { it < 100 }?.toString() ?: "99+"
    }
}