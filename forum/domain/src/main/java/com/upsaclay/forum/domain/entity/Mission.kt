package com.upsaclay.forum.domain.entity

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User

data class Mission(
    val id: String,
    val title: String,
    val description: String,
    val schoolLevels: List<SchoolLevel>,
    val date: String,
    val frequency: String?,
    val managers: List<User>,
    val participants: List<User>,
    val participantMax: Int,
    val tasks: List<String>,
    val imageUrl: String? = null,
)
