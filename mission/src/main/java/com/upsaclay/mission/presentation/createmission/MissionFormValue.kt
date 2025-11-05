package com.upsaclay.mission.presentation.createmission

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.domain.entity.MissionTask
import java.time.LocalDate

data class MissionFormValue(
    val title: String,
    val description: String,
    val schoolLevels: List<SchoolLevel>,
    val selectedSchoolLevels: List<SchoolLevel>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val frequency: String,
    val participantNumber: String,
    val selectedManagers: List<User>,
    val missionTasks: List<MissionTask>,
    val imageUri: String?,
)