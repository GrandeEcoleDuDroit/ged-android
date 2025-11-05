package com.upsaclay.mission.domain

import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.domain.usersFixture
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
    title = "Long title example to test ellipsis in the mission card component",
    description = """
        This is the description of the first mission. It can be quite long and detailed. It provides all the necessary information about the mission.
        The mission aims to help students improve their skills and gain practical experience in various fields. Participants will have the opportunity to work on real projects and collaborate with professionals.
        We encourage all interested students to apply and take advantage of this unique learning experience.
        """.trimIndent(),
    schoolLevels = listOf(
        SchoolLevel.GED_1,
        SchoolLevel.GED_2,
        SchoolLevel.GED_3
    ),
    date = LocalDateTime.now(),
    startDate = LocalDate.now(),
    endDate = LocalDate.now().plusDays(1),
    duration = "Once a week",
    managers = usersFixture,
    participants = usersFixture,
    maxParticipants = 20,
    tasks = tasksFixture,
    state = MissionState.Published(),
)

val missionsFixture = listOf(
    missionFixture,
    missionFixture.copy(
        id = 2,
        title = "Second mission",
        description = "A short description for the second mission.",
        managers = listOf(userFixture2),
        participants = listOf(userFixture),
        schoolLevels = listOf(SchoolLevel.GED_1)
    ),
    missionFixture.copy(
        id = 3,
        title = "Third mission",
        description = "The third mission has a medium-length description to provide some context.",
        managers = listOf(userFixture2, userFixture),
        participants = listOf(userFixture2, userFixture),
        schoolLevels = listOf(SchoolLevel.GED_1, SchoolLevel.GED_2, SchoolLevel.GED_3)
    ),
    missionFixture.copy(
        id = 4,
        title = "Fourth mission",
        description = "The third mission has a medium-length description to provide some context.",
        state = MissionState.Error(),
        managers = listOf(userFixture2, userFixture),
        participants = listOf(userFixture2, userFixture),
        schoolLevels = listOf(SchoolLevel.GED_1, SchoolLevel.GED_2, SchoolLevel.GED_3)
    ),
)