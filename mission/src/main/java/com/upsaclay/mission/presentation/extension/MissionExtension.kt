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

    return sortedWith(
        compareBy(::priority)
            .thenComparator { lhs, rhs ->
                when (priority(lhs)) {
                    Priority.FIRST -> compareValues(rhs.date, lhs.date)
                    Priority.SECOND -> compareValues(lhs.date, rhs.date)
                    Priority.THIRD -> compareValues(rhs.endDate, lhs.endDate)
                }
            }
    )
}