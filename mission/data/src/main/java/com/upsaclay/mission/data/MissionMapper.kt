package com.upsaclay.mission.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upsaclay.common.data.UrlUtils
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.common.domain.extensions.toLocalDateUTC
import com.upsaclay.mission.data.local.LocalMission
import com.upsaclay.mission.data.remote.RemoteMission
import com.upsaclay.mission.data.remote.RemoteMissionTask
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask

fun LocalMission.toMission(): Mission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val missionTasksType = object : TypeToken<List<MissionTask>>() {}.type
    val usersType = object : TypeToken<List<User>>() {}.type

    val schoolLevelNumbers = gson.fromJson<List<Int>>(missionSchoolLevels, schoolLevelNumbersType)

    return Mission(
        id = missionId,
        title = missionTitle,
        description = missionDescription,
        schoolLevels = schoolLevelNumbers.mapNotNull(SchoolLevel::fromNumber),
        date = missionDate.toLocalDateTimeUTC(),
        startDate = missionStartDate.toLocalDateUTC(),
        endDate = missionEndDate.toLocalDateUTC(),
        frequency = missionFrequency,
        managers = gson.fromJson(missionManagers, usersType),
        participants = gson.fromJson(missionParticipants, usersType),
        maxParticipants = missionMaxParticipants,
        tasks = missionTasks?.let { gson.fromJson(it, missionTasksType) } ?: emptyList(),
        state = MissionState.fromString(missionState, missionImageReference)
    )
}

fun Mission.toLocal(): LocalMission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val usersType = object : TypeToken<List<User>>() {}.type
    val missionTasksType = object : TypeToken<List<MissionTask>>() {}.type

    val schoolLevelNumbers = schoolLevels.map { it.number }
    val imageReference = when (val state = state) {
        is MissionState.Draft -> state.imageUri
        is MissionState.Publishing -> state.imagePath
        is MissionState.Published -> UrlUtils.extractFileName(state.imageUrl)
        is MissionState.Error -> state.imagePath
    }

    return LocalMission(
        missionId = id,
        missionTitle = title,
        missionDescription = description,
        missionSchoolLevels = gson.toJson(schoolLevelNumbers, schoolLevelNumbersType),
        missionDate = date.toEpochMilliUTC(),
        missionStartDate = startDate.toEpochMilliUTC(),
        missionEndDate = endDate.toEpochMilliUTC(),
        missionFrequency = frequency,
        missionManagers = gson.toJson(managers, usersType),
        missionParticipants = gson.toJson(participants, usersType),
        missionMaxParticipants = maxParticipants,
        missionTasks = gson.toJson(tasks, missionTasksType),
        missionImageReference = imageReference,
        missionState = state.toString()
    )
}

fun Mission.toRemote(imageFileName: String?): RemoteMission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val userIdsType = object : TypeToken<List<String>>() {}.type

    val schoolLevelNumbers = schoolLevels.map { it.number }
    val remoteMissionTasks = tasks.map { it.toRemote(id) }
    val managerIds = managers.map { it.id }
    val participantIds = participants.map { it.id }

    return RemoteMission(
        missionId = id,
        missionTitle = title,
        missionDescription = description,
        missionSchoolLevels = gson.toJson(schoolLevelNumbers, schoolLevelNumbersType),
        missionDate = date.toEpochMilliUTC(),
        missionStartDate = startDate.toEpochMilliUTC(),
        missionEndDate = endDate.toEpochMilliUTC(),
        missionFrequency = frequency,
        missionManagerIds = gson.toJson(managerIds, userIdsType),
        missionParticipantIds = gson.toJson(participantIds, userIdsType),
        missionMaxParticipants = maxParticipants,
        missionTasks = gson.toJson(remoteMissionTasks),
        missionImageFileName = imageFileName
    )
}

fun MissionTask.toRemote(missionId: Int) = RemoteMissionTask(
    id = id,
    value = value,
    missionId = missionId
)