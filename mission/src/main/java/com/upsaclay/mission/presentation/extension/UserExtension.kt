package com.upsaclay.mission.presentation.extension

import com.upsaclay.common.domain.entity.User

fun List<User>.managerSorting() : List<User> =
    this
        .sortedBy { it.fullName }
        .sortedByDescending { it.admin }