package com.upsaclay.mission.data.local

import com.google.gson.annotations.SerializedName
import com.upsaclay.mission.data.MissionTaskField.Local.MISSION_TASK_ID
import com.upsaclay.mission.data.MissionTaskField.Local.MISSION_TASK_VALUE

data class LocalMissionTask(
    @SerializedName(MISSION_TASK_ID)
    val missionTaskId: String,
    @SerializedName(MISSION_TASK_VALUE)
    val missionTaskValue: String
)
