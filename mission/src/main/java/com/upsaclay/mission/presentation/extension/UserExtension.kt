package com.upsaclay.mission.presentation.extension

import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.domain.entity.Mission

fun List<User>.managerSorting() : List<User> =
    this
        .sortedBy { it.fullName }
        .sortedByDescending { it.admin }

fun List<User>.managerSorting(mission: Mission) : List<User> =
    this
        .sortedBy { it.fullName }
        .sortedByDescending { it.admin }
        .sortedByDescending { mission.managers.contains(it) }