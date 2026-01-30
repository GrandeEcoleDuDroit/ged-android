package com.upsaclay.mission.presentation.extension

import com.upsaclay.common.domain.entity.ComparatorResult
import com.upsaclay.common.domain.entity.Priority
import com.upsaclay.mission.domain.entity.Mission

fun List<Mission>.missionSorting(): List<Mission> {
    fun priority(mission: Mission): Priority {
        return when {
            mission.state !is Mission.MissionState.Published -> Priority.FIRST
            !mission.completed -> Priority.SECOND
            else -> Priority.THIRD
        }
    }

    fun compareNonCompletedMission(lhs: Mission, rhs: Mission): Int {
        return compareValues(lhs.startDate, rhs.startDate).takeUnless { it == ComparatorResult.EQUALS }
            ?: compareValues(lhs.endDate, rhs.endDate).takeUnless { it == ComparatorResult.EQUALS }
            ?: compareValues(rhs.date, lhs.date)
    }

    return sortedWith(
        compareBy(::priority)
            .thenComparator { lhs, rhs ->
                when (priority(lhs)) {
                    Priority.FIRST -> compareValues(rhs.date, lhs.date)
                    Priority.SECOND -> compareNonCompletedMission(lhs, rhs)
                    Priority.THIRD -> compareValues(rhs.endDate, lhs.endDate)
                }
            }
    )
}