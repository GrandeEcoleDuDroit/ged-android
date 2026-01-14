package com.upsaclay.mission.data.remote.models

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.mission.data.MissionField.Remote.Inbound.MISSION_MANAGERS
import com.upsaclay.mission.data.MissionField.Remote.Inbound.MISSION_PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_DATE
import com.upsaclay.mission.data.MissionField.Remote.MISSION_DESCRIPTION
import com.upsaclay.mission.data.MissionField.Remote.MISSION_DURATION
import com.upsaclay.mission.data.MissionField.Remote.MISSION_END_DATE
import com.upsaclay.mission.data.MissionField.Remote.MISSION_ID
import com.upsaclay.mission.data.MissionField.Remote.MISSION_IMAGE_FILE_NAME
import com.upsaclay.mission.data.MissionField.Remote.MISSION_MAX_PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_SCHOOL_LEVELS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_START_DATE
import com.upsaclay.mission.data.MissionField.Remote.MISSION_TASKS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_TITLE
import com.upsaclay.mission.data.MissionField.Remote.Outbound.MISSION_MANAGER_IDS
import com.upsaclay.mission.data.MissionField.Remote.Outbound.MISSION_PARTICIPANT_IDS

data class OutboundRemoteMission(
    @SerializedName(MISSION_ID)
    val missionId: String,
    @SerializedName(MISSION_TITLE)
    val missionTitle: String,
    @SerializedName(MISSION_DESCRIPTION)
    val missionDescription: String,
    @SerializedName(MISSION_SCHOOL_LEVELS)
    val missionSchoolLevels: String,
    @SerializedName(MISSION_DATE)
    val missionDate: Long,
    @SerializedName(MISSION_START_DATE)
    val missionStartDate: Long,
    @SerializedName(MISSION_END_DATE)
    val missionEndDate: Long,
    @SerializedName(MISSION_DURATION)
    val missionDuration: String?,
    @SerializedName(MISSION_MANAGER_IDS)
    val missionManagerIds: String,
    @SerializedName(MISSION_PARTICIPANT_IDS)
    val missionParticipantIds: String,
    @SerializedName(MISSION_MAX_PARTICIPANTS)
    val missionMaxParticipants: Int,
    @SerializedName(MISSION_TASKS)
    val missionTasks: String,
    @SerializedName(MISSION_IMAGE_FILE_NAME)
    val missionImageFileName: String?
)

data class InboundRemoteMission(
    @SerializedName(MISSION_ID)
    val missionId: String,
    @SerializedName(MISSION_TITLE)
    val missionTitle: String,
    @SerializedName(MISSION_DESCRIPTION)
    val missionDescription: String,
    @SerializedName(MISSION_SCHOOL_LEVELS)
    val missionSchoolLevels: String?,
    @SerializedName(MISSION_DATE)
    val missionDate: Long,
    @SerializedName(MISSION_START_DATE)
    val missionStartDate: Long,
    @SerializedName(MISSION_END_DATE)
    val missionEndDate: Long,
    @SerializedName(MISSION_DURATION)
    val missionDuration: String?,
    @SerializedName(MISSION_MANAGERS)
    val missionManagers: List<OracleUser>,
    @SerializedName(MISSION_PARTICIPANTS)
    val missionParticipants: List<OracleUser>?,
    @SerializedName(MISSION_MAX_PARTICIPANTS)
    val missionMaxParticipants: Int,
    @SerializedName(MISSION_TASKS)
    val missionTasks: List<RemoteMissionTask>?,
    @SerializedName(MISSION_IMAGE_FILE_NAME)
    val missionImageFileName: String?
)