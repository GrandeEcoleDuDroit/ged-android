package com.upsaclay.forum.domain

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.forum.domain.entity.Mission

val missionFixture = Mission(
    id = "1",
    title = "Randonnée",
    description = "Organisation de la randonnée de l’année, conviant touts les étudiants de Paris-Sacalay",
    schoolLevels = listOf(
        SchoolLevel.GED_1,
        SchoolLevel.GED_2,
        SchoolLevel.GED_3,
        SchoolLevel.GED_4
    ),
    date = "Janvier - Avril",
    frequency = "Environ 10 - 20h par étudiant",
    managers = listOf(userFixture),
    participants = listOf(userFixture2),
    participantMax = 5,
    tasks = listOf("Guider les participants", "Animer la soirée")
)

val missionsFixture = listOf(
    missionFixture,
    missionFixture.copy(
        id = "2",
        title = "Randonnée 2",
        description = "Organisation de la randonnée de l’année, conviant touts les étudiants de Paris-Sacalay",
        managers = listOf(userFixture2),
        participants = listOf(userFixture),
        schoolLevels = listOf(SchoolLevel.GED_1)
    ),
    missionFixture.copy(
        id = "3",
        title = "Randonnée 3",
        description = "Organisation de la randonnée de l’année, conviant touts les étudiants de Paris-Sacalay",
        managers = listOf(userFixture2, userFixture),
        participants = listOf(userFixture2, userFixture),
        schoolLevels = listOf(SchoolLevel.GED_1, SchoolLevel.GED_2, SchoolLevel.GED_3)
    )
)