package com.upsaclay.mission.presentation.extension

import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.domain.entity.Mission

fun List<User>.missionManagerSorting() : List<User> =
    sortedWith(
        compareByDescending<User> { it.admin }
            .thenBy { it.fullName }
    )

fun List<User>.missionManagerSorting(mission: Mission): List<User> {
    val managerIds = mission.managers.map { it.id }.toSet()

    fun priority(user: User): Int = when {
        managerIds.contains(user.id) -> 0
        user.admin -> 1
        else -> 2
    }

    return sortedWith(
        compareBy(::priority)
            .thenBy { it.fullName }
    )
}
