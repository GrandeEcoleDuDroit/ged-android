package com.upsaclay.mission.data.mapper

import com.upsaclay.mission.data.local.LocalMissionTask
import com.upsaclay.mission.data.remote.models.RemoteMissionTask
import com.upsaclay.mission.domain.entity.MissionTask


fun MissionTask.toLocal() = LocalMissionTask(
    missionTaskId = id,
    missionTaskValue = value
)

fun MissionTask.toRemote() = RemoteMissionTask(
    missionTaskId = id,
    missionTaskValue = value
)

fun LocalMissionTask.toMissionTask() = MissionTask(
    id = missionTaskId,
    value = missionTaskValue
)

fun RemoteMissionTask.toMissionTask() = MissionTask(
    id = missionTaskId,
    value = missionTaskValue
)