package com.upsaclay.mission.domain

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import java.time.LocalDate
import java.time.LocalDateTime

val missionTaskFixture = MissionTask(1, "Task 1")

val tasksFixture = listOf(
    missionTaskFixture,
    missionTaskFixture.copy(id = 2, value = "Task 2"),
    missionTaskFixture.copy(id = 3, value = "Task 3")
)

val missionFixture = Mission(
    id = 1,
    title = "Randonnée",
    description = "Organisation de la randonnée de l’année, conviant touts les étudiants de Paris-Sacalay",
    schoolLevels = listOf(
        SchoolLevel.GED_1,
        SchoolLevel.GED_2,
        SchoolLevel.GED_3,
        SchoolLevel.GED_4
    ),
    date = LocalDateTime.now(),
    startDate = LocalDate.now(),
    endDate = LocalDate.now().plusDays(1),
    frequency = "Environ 10 - 20h par étudiant",
    managers = listOf(userFixture),
    participants = listOf(userFixture2),
    maxParticipants = 5,
    missionTasks = tasksFixture,
    state = MissionState.Published(),
)

val missionsFixture = listOf(
    missionFixture,
    missionFixture.copy(
        id = 2,
        title = "Randonnée 2",
        description = "Organisation de la randonnée de l’année, conviant touts les étudiants de Paris-Sacalay",
        managers = listOf(userFixture2),
        participants = listOf(userFixture),
        schoolLevels = listOf(SchoolLevel.GED_1)
    ),
    missionFixture.copy(
        id = 3,
        title = "Randonnée 3",
        description = "Organisation de la randonnée de l’année, conviant touts les étudiants de Paris-Sacalay",
        managers = listOf(userFixture2, userFixture),
        participants = listOf(userFixture2, userFixture),
        schoolLevels = listOf(SchoolLevel.GED_1, SchoolLevel.GED_2, SchoolLevel.GED_3)
    )
)