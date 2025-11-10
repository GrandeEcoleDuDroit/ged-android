package com.upsaclay.mission.data.remote

import com.google.gson.annotations.SerializedName
import com.upsaclay.mission.data.MissionTaskField.Remote.MISSION_TASK_ID
import com.upsaclay.mission.data.MissionTaskField.Remote.MISSION_TASK_VALUE

data class RemoteMissionTask(
    @SerializedName(MISSION_TASK_ID)
    val missionTaskId: String,
    @SerializedName(MISSION_TASK_VALUE)
    val missionTaskValue: String
)