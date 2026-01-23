package com.upsaclay.mission.presentation.extension

import com.upsaclay.common.domain.entity.User

fun List<User>.missionManagerSorting() : List<User> =
    sortedWith(
        compareByDescending<User> { it.admin }
            .thenBy { it.fullName }
    )

fun List<User>.missionManagerSorting(currentManagers: List<User>): List<User> {
    val currentManagerIds = currentManagers.map { it.id }.toSet()

    fun priority(user: User): Int = when {
        currentManagerIds.contains(user.id) -> 0
        user.admin -> 1
        else -> 2
    }

    return sortedWith(
        compareBy(::priority)
            .thenBy { it.fullName }
    )
}
