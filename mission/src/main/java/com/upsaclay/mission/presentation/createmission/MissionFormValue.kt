package com.upsaclay.mission.presentation.createmission

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.domain.entity.MissionTask
import java.time.LocalDate

data class MissionFormValue(
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val allSchoolLevels: List<SchoolLevel>,
    val schoolLevels: List<SchoolLevel>,
    val duration: String,
    val maxParticipants: String,
    val managers: List<User>,
    val tasks: List<MissionTask>,
    val imageReference: String?
)