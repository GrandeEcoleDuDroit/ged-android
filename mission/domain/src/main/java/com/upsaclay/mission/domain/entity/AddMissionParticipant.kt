package com.upsaclay.mission.domain.entity

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User

data class AddMissionParticipant(
    val missionId: String,
    val schoolLevels: List<SchoolLevel>,
    val maxParticipants: Int,
    val participantsNumber: Int,
    val currentUser: User
)
