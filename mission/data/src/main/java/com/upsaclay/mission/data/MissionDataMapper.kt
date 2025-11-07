package com.upsaclay.mission.data

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
import com.upsaclay.mission.data.remote.InboundRemoteMission
import com.upsaclay.mission.data.remote.OutboundRemoteMission
import com.upsaclay.mission.data.remote.RemoteMissionTask
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionState.Companion.DRAFT
import com.upsaclay.mission.domain.entity.MissionState.Companion.ERROR
import com.upsaclay.mission.domain.entity.MissionState.Companion.PUBLISHED
import com.upsaclay.mission.domain.entity.MissionState.Companion.PUBLISHING
import com.upsaclay.mission.domain.entity.MissionTask

fun LocalMission.toMission(): Mission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val missionTasksType = object : TypeToken<List<MissionTask>>() {}.type
    val usersType = object : TypeToken<List<User>>() {}.type

    val schoolLevelNumbers = gson.fromJson<List<Int>>(missionSchoolLevels, schoolLevelNumbersType)
    val state = when (missionState) {
        DRAFT -> MissionState.Draft(missionImageReference)
        PUBLISHING -> MissionState.Publishing(missionImageReference)
        PUBLISHED -> MissionState.Published(UrlUtils.formatOracleBucketUrl(missionImageReference))
        else -> MissionState.Error(missionImageReference)
    }

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
        tasks = missionTasks?.let { gson.fromJson(it, missionTasksType) } ?: emptyList(),
        state = state
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
        missionDuration = duration,
        missionManagers = gson.toJson(managers, usersType),
        missionParticipants = gson.toJson(participants, usersType),
        missionMaxParticipants = maxParticipants,
        missionTasks = gson.toJson(tasks, missionTasksType),
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
    id = id,
    value = value
)

fun InboundRemoteMission.toMission(): Mission {
    val gson = Gson()
    val schoolLevelNumbersType = object : TypeToken<List<Int>>() {}.type
    val userType = object : TypeToken<List<ServerUser>>() {}.type
    val missionTasksType = object : TypeToken<List<RemoteMissionTask>>() {}.type

    val schoolLevels = gson.fromJson<List<Int>>(missionSchoolLevels, schoolLevelNumbersType)
    val managers = gson.fromJson<List<ServerUser>>(missionManagers, userType)
    val missionTasks = gson.fromJson<List<RemoteMissionTask>>(missionTasks, missionTasksType)
    val participants = gson.fromJson<List<ServerUser>>(missionParticipants, userType)

    return Mission(
        id = missionId,
        title = missionTitle,
        description = missionDescription,
        schoolLevels = schoolLevels.map(SchoolLevel::fromNumber),
        date = missionDate.toLocalDateTimeUTC(),
        startDate = missionStartDate.toLocalDateUTC(),
        endDate = missionEndDate.toLocalDateUTC(),
        duration = missionDuration,
        managers = managers.map(ServerUser::toUser),
        participants = participants.map(ServerUser::toUser),
        maxParticipants = missionMaxParticipants,
        tasks = missionTasks.map(RemoteMissionTask::toMissionTask),
        state = MissionState.Published(UrlUtils.formatOracleBucketUrl(missionImageFileName))
    )
}

fun RemoteMissionTask.toMissionTask() = MissionTask(
    id = id,
    value = value
)