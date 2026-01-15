package com.upsaclay.mission.presentation.extension

import com.upsaclay.mission.domain.entity.Mission

fun List<Mission>.missionSorting(): List<Mission> {
    fun priority(mission: Mission): Int {
        return when  {
            mission.state !is Mission.MissionState.Published -> 0
            !mission.completed -> 1
            else -> 2
        }
    }

    return sortedWith(
        compareBy(::priority)
            .thenBy {
                when (priority(it)) {
                    0 -> it.date
                    1 -> it.startDate
                    2 -> it.endDate
                    else -> it.date
                }
            }
    )
}