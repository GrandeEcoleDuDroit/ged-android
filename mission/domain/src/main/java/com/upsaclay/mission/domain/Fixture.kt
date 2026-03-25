package com.upsaclay.mission.domain

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import java.time.LocalDate
import java.time.LocalDateTime

val missionTaskFixture = MissionTask("1", "Task 1")

val missionTasksFixture = listOf(
    missionTaskFixture,
    missionTaskFixture.copy(id = "2", value = "Task 2"),
    missionTaskFixture.copy(id = "3", value = "Task 3")
)

val missionFixture = Mission(
    id = "1",
    title = "Randonée en forêt",
    description = "Nous vous convions à une petite randonnée en forêt entre camarade. " +
            "Ce sera l'occasion de se détendre et de profiter du grand air.",
    date = LocalDateTime.now(),
    startDate = LocalDate.now(),
    endDate = LocalDate.now().plusDays(2),
    schoolLevels = SchoolLevel.all,
    duration = "Toute la journée",
    managers = listOf(userFixture, userFixture2),
    participants = listOf(usersFixture[2], usersFixture[3]),
    maxParticipants = 10,
    tasks = missionTasksFixture,
    state = MissionState.Published(
        imageUrl = "https://plus.unsplash.com/premium_photo-1666874681316-023c0fc7a4be?q=80&w=2340&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    )
)

val missionsFixture = listOf(
    missionFixture,
    missionFixture.copy(
        id = "2",
        title = "Fête de l'école 🎉",
        description = "Ce week-end aura lieu la fête de l'école. Parents, élèves et enseignants " +
                "sont invités à partager un moment convivial autour de jeux, animations " +
                "et spectacles préparés par les enfants.",
        managers = listOf(userFixture2),
        startDate = LocalDate.now().plusDays(2),
        endDate = LocalDate.now().plusDays(5),
        participants = emptyList(),
        maxParticipants = 5,
        schoolLevels = SchoolLevel.all,
        state = MissionState.Published(
            imageUrl = "https://plus.unsplash.com/premium_photo-1663839411973-af76a84f5ffe?q=80&w=1287&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        )
    ),
    missionFixture.copy(
        id = "3",
        title = "Recolte de fond pour le voyage en Islande",
        description = """
            Mission consistant à récolter des fonds afin de permettre aux élèves
            de participer à un voyage scolaire en Islande.
        """.trimIndent(),
        managers = listOf(userFixture, userFixture2),
        participants = listOf(usersFixture[0], usersFixture[1], usersFixture[3]),
        endDate = LocalDate.now().plusDays(10),
        maxParticipants = 3,
        schoolLevels = listOf(SchoolLevel.LEVEL_3, SchoolLevel.LEVEL_4),
        tasks = listOf(
            MissionTask(
                id = "1",
                value = "Organiser des événements de collecte de fonds (vente de gâteaux, tombola, etc.)"
            ),
            MissionTask(
                id = "2",
                value = "Contacter des sponsors ou partenaires potentiels"
            ),
            MissionTask(
                id = "3",
                value = "Gérer les inscriptions et les paiements des participants"
            )
        ),
        state = MissionState.Published(
            imageUrl = "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        )
    ),
    missionFixture.copy(
        id = "4",
        title = "Supervision épreuves",
        description = """
            Supervision des épreuves afin de veiller au bon déroulement et au respect des consignes.
        """.trimIndent(),
        startDate = LocalDate.now().minusDays(2),
        endDate = LocalDate.now().plusDays(1),
        managers = listOf(userFixture2),
        participants = listOf(usersFixture[0], usersFixture[1], usersFixture[3]),
        maxParticipants = 3,
        schoolLevels = listOf(SchoolLevel.LEVEL_2)
    )
)