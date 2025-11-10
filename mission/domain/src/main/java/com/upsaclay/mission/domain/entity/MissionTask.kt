package com.upsaclay.mission.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class MissionTask(
    val id: String,
    val value: String
)