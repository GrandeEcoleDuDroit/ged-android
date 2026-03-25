package com.upsaclay.mission.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.common.data.toRemote
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.common.domain.extensions.toLocalDateUTC
import com.upsaclay.mission.data.extractFileNameFromPath
import com.upsaclay.mission.data.formatUrl
import com.upsaclay.mission.data.local.LocalMission
import com.upsaclay.mission.data.local.LocalMissionTask
import com.upsaclay.mission.data.remote.models.InboundRemoteMission
import com.upsaclay.mission.data.remote.models.OutboundRemoteMission
import com.upsaclay.mission.data.remote.models.RemoteMissionReport
import com.upsaclay.mission.data.remote.models.RemoteMissionTask
import com.upsaclay.mission.domain.MissionUtils
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.domain.entity.MissionTask

fun Mission.toRemote(): OutboundRemoteMission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val userIdsType = object : TypeToken<List<String>>() {}.type

    val schoolLevelNumbers = schoolLevels.map { it.number }
    val remoteMissionTasks = tasks.map(MissionTask::toRemote)
    val managerIds = managers.map { it.id }
    val participantIds = participants.map { it.id }
    val imageFileName = when (val state = state) {
        is MissionState.Draft -> null
        is MissionState.Publishing -> MissionUtils.Image.extractFileNameFromPath(state.imagePath)
        is MissionState.Published -> MissionUtils.Image.getFileName(state.imageUrl)
        is MissionState.Error -> MissionUtils.Image.extractFileNameFromPath(state.imagePath)
    }

    return OutboundRemoteMission(
        missionId = id,
        missionTitle = title,
        missionDescription = description,
        missionSchoolLevels = gson.toJson(schoolLevelNumbers, schoolLevelNumbersType),
        missionDate = date.toEpochMilliUTC(),
        missionStartDate = startDate.toEpochMilliUTC(),
        missionEndDate = endDate.toEpochMilliUTC(),
        missionDuration = duration,
        missionManagerIds = gson.toJson(managerIds, userIdsType),
        missionParticipantIds = gson.toJson(participantIds, userIdsType),
        missionMaxParticipants = maxParticipants,
        missionTasks = gson.toJson(remoteMissionTasks),
        missionImageFileName = imageFileName
    )
}

fun Mission.toLocal(): LocalMission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val usersType = object : TypeToken<List<User>>() {}.type
    val localMissionTasksType = object : TypeToken<List<LocalMissionTask>>() {}.type

    val schoolLevelNumbers = schoolLevels.map { it.number }
    val imageFileName = when (val state = state) {
        is MissionState.Draft -> null
        is MissionState.Publishing -> MissionUtils.Image.extractFileNameFromPath(state.imagePath)
        is MissionState.Published -> MissionUtils.Image.getFileName(state.imageUrl)
        is MissionState.Error -> MissionUtils.Image.extractFileNameFromPath(state.imagePath)
    }
    val localMissionTasks = tasks.map(MissionTask::toLocal)

    return LocalMission(
        missionId = id,
        missionTitle = title,
        missionDescription = description,
        missionSchoolLevels = gson.toJson(schoolLevelNumbers, schoolLevelNumbersType),
        missionDate = date.toEpochMilliUTC(),
        missionStartDate = startDate.toEpochMilliUTC(),
        missionEndDate = endDate.toEpochMilliUTC(),
        missionDuration = duration,
        missionManagers = gson.toJson(managers, usersType),
        missionParticipants = gson.toJson(participants, usersType),
        missionMaxParticipants = maxParticipants,
        missionTasks = gson.toJson(localMissionTasks, localMissionTasksType),
        missionImageFileName = imageFileName,
        missionState = state.toString()
    )
}

fun LocalMission.toMission(getImagePath: (String) -> String): Mission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val missionTasksType = object : TypeToken<List<LocalMissionTask>>() {}.type
    val usersType = object : TypeToken<List<User>>() {}.type

    val schoolLevelNumbers = gson.fromJson<List<Int>>(missionSchoolLevels, schoolLevelNumbersType)
    val state = when (missionState) {
        MissionState.Draft.TYPE -> MissionState.Draft
        MissionState.Publishing.TYPE -> MissionState.Publishing(missionImageFileName?.let(getImagePath))
        MissionState.Published.TYPE -> MissionState.Published(MissionUtils.Image.formatUrl(missionImageFileName))
        else -> MissionState.Error(missionImageFileName?.let(getImagePath))
    }
    val tasks = missionTasks?.let {
        gson.fromJson<List<LocalMissionTask>>(it, missionTasksType)
    }?.map(LocalMissionTask::toMissionTask) ?: emptyList()

    return Mission(
        id = missionId,
        title = missionTitle,
        description = missionDescription,
        schoolLevels = schoolLevelNumbers.map(SchoolLevel::fromNumber),
        date = missionDate.toLocalDateTimeUTC(),
        startDate = missionStartDate.toLocalDateUTC(),
        endDate = missionEndDate.toLocalDateUTC(),
        duration = missionDuration,
        managers = gson.fromJson(missionManagers, usersType),
        participants = gson.fromJson(missionParticipants, usersType),
        maxParticipants = missionMaxParticipants,
        tasks = tasks,
        state = state
    )
}

fun InboundRemoteMission.toMission(): Mission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type

    val schoolLevelNumbers = missionSchoolLevels?.let {
        gson.fromJson<List<Int>>(it, schoolLevelNumbersType)
    } ?: emptyList()

    return Mission(
        id = missionId,
        title = missionTitle,
        description = missionDescription,
        schoolLevels = schoolLevelNumbers.map { SchoolLevel.fromNumber(it) },
        date = missionDate.toLocalDateTimeUTC(),
        startDate = missionStartDate.toLocalDateUTC(),
        endDate = missionEndDate.toLocalDateUTC(),
        duration = missionDuration,
        managers = missionManagers.map(OracleUser::toUser),
        participants = missionParticipants?.map(OracleUser::toUser) ?: emptyList(),
        maxParticipants = missionMaxParticipants,
        tasks = missionTasks?.map(RemoteMissionTask::toMissionTask) ?: emptyList(),
        state = MissionState.Published(MissionUtils.Image.formatUrl(missionImageFileName))
    )
}

internal fun MissionReport.toRemote() = RemoteMissionReport(
    missionId = missionId,
    reporter = reporter.toRemote(),
    reason = reason
)