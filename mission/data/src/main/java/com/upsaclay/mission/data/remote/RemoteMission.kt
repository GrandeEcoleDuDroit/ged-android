package com.upsaclay.mission.data.remote

import com.google.gson.annotations.SerializedName
import com.upsaclay.mission.data.MissionField.Local.MISSION_ID
import com.upsaclay.mission.data.MissionField.Remote.MISSION_DATE
import com.upsaclay.mission.data.MissionField.Remote.MISSION_DESCRIPTION
import com.upsaclay.mission.data.MissionField.Remote.MISSION_END_DATE
import com.upsaclay.mission.data.MissionField.Remote.MISSION_FREQUENCY
import com.upsaclay.mission.data.MissionField.Remote.MISSION_IMAGE_FILE_NAME
import com.upsaclay.mission.data.MissionField.Remote.MISSION_MANAGERS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_MAX_PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_SCHOOL_LEVEL
import com.upsaclay.mission.data.MissionField.Remote.MISSION_START_DATE
import com.upsaclay.mission.data.MissionField.Remote.MISSION_TASKS
import com.upsaclay.mission.data.MissionField.Remote.MISSION_TITLE

data class RemoteMission(
    @SerializedName(MISSION_ID)
    val missionId: Int,
    @SerializedName(MISSION_TITLE)
    val missionTitle: String,
    @SerializedName(MISSION_DESCRIPTION)
    val missionDescription: String,
    @SerializedName(MISSION_SCHOOL_LEVEL)
    val missionSchoolLevels: String,
    @SerializedName(MISSION_DATE)
    val missionDate: Long,
    @SerializedName(MISSION_START_DATE)
    val missionStartDate: Long,
    @SerializedName(MISSION_END_DATE)
    val missionEndDate: Long,
    @SerializedName(MISSION_FREQUENCY)
    val missionFrequency: String,
    @SerializedName(MISSION_MANAGERS)
    val missionManagerIds: String,
    @SerializedName(MISSION_PARTICIPANTS)
    val missionParticipantIds: String,
    @SerializedName(MISSION_MAX_PARTICIPANTS)
    val missionMaxParticipants: Int,
    @SerializedName(MISSION_TASKS)
    val missionTasks: String,
    @SerializedName(MISSION_IMAGE_FILE_NAME)
    val missionImageFileName: String?
)
