package com.upsaclay.mission.data.remote.models

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField.Server.USER_ID
import com.upsaclay.common.data.UserField.Server.USER_SCHOOL_LEVEL
import com.upsaclay.mission.data.MissionField.Remote.MISSION_ID
import com.upsaclay.mission.data.MissionField.Remote.MISSION_MAX_PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_SCHOOL_LEVELS

private const val MISSION_PARTICIPANTS_NUMBER = "MISSION_PARTICIPANTS_NUMBER"

data class RemoteAddMissionParticipant(
    @SerializedName(MISSION_ID)
    val missionId: String,
    @SerializedName(MISSION_SCHOOL_LEVELS)
    val missionSchoolLevels: List<Int>,
    @SerializedName(MISSION_MAX_PARTICIPANTS)
    val missionMaxParticipants: Int,
    @SerializedName(MISSION_PARTICIPANTS_NUMBER)
    val missionParticipantsNumber: Int,
    @SerializedName(USER_ID)
    val userId: String,
    @SerializedName(USER_SCHOOL_LEVEL)
    val userSchoolLevel: Int
)