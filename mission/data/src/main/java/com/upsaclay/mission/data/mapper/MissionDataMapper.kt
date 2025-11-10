package com.upsaclay.mission.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upsaclay.common.data.UrlUtils
import com.upsaclay.common.data.remote.model.ServerUser
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.common.domain.extensions.toLocalDateUTC
import com.upsaclay.mission.data.local.LocalMission
import com.upsaclay.mission.data.local.LocalMissionTask
import com.upsaclay.mission.data.remote.InboundRemoteMission
import com.upsaclay.mission.data.remote.OutboundRemoteMission
import com.upsaclay.mission.data.remote.RemoteMissionReport
import com.upsaclay.mission.data.remote.RemoteMissionTask
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask

fun LocalMission.toMission(): Mission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val missionTasksType = object : TypeToken<List<LocalMissionTask>>() {}.type
    val usersType = object : TypeToken<List<User>>() {}.type

    val schoolLevelNumbers = gson.fromJson<List<Int>>(missionSchoolLevels, schoolLevelNumbersType)
    val state = when (missionState) {
        MissionState.Draft.TYPE -> MissionState.Draft(missionImageReference)
        MissionState.Publishing.TYPE -> MissionState.Publishing(missionImageReference)
        MissionState.Published.TYPE -> MissionState.Published(UrlUtils.formatOracleBucketUrl(missionImageReference))
        else -> MissionState.Error(missionImageReference)
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

fun Mission.toLocal(): LocalMission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val usersType = object : TypeToken<List<User>>() {}.type
    val localMissionTasksType = object : TypeToken<List<LocalMissionTask>>() {}.type

    val schoolLevelNumbers = schoolLevels.map { it.number }
    val imageReference = when (val state = state) {
        is MissionState.Draft -> state.imageUri
        is MissionState.Publishing -> state.imagePath
        is MissionState.Published -> UrlUtils.extractFileName(state.imageUrl)
        is MissionState.Error -> state.imagePath
    }
    val localMissionsTask = tasks.map(MissionTask::toLocal)

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
        missionTasks = gson.toJson(localMissionsTask, localMissionTasksType),
        missionImageReference = imageReference,
        missionState = state.toString()
    )
}

fun Mission.toRemote(imageFileName: String?): OutboundRemoteMission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val userIdsType = object : TypeToken<List<String>>() {}.type

    val schoolLevelNumbers = schoolLevels.map { it.number }
    val remoteMissionTasks = tasks.map(MissionTask::toRemote)
    val managerIds = managers.map { it.id }
    val participantIds = participants.map { it.id }

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

fun MissionTask.toRemote() = RemoteMissionTask(
    missionTaskId = id,
    missionTaskValue = value
)

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
        managers = missionManagers.map(ServerUser::toUser),
        participants = missionParticipants?.map(ServerUser::toUser) ?: emptyList(),
        maxParticipants = missionMaxParticipants,
        tasks = missionTasks?.map(RemoteMissionTask::toMissionTask) ?: emptyList(),
        state = MissionState.Published(UrlUtils.formatOracleBucketUrl(missionImageFileName))
    )
}

internal fun MissionReport.toRemote() = RemoteMissionReport(
    missionId = missionId,
    userInfo = userInfo.toRemote(),
    reason = reason.toString()
)

internal fun MissionReport.UserInfo.toRemote() = RemoteMissionReport.RemoteUserInfo(
    fullName = fullName,
    email = email
)
