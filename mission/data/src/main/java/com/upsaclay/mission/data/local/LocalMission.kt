package com.upsaclay.mission.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.mission.data.MissionField.Local.MISSION_DATE
import com.upsaclay.mission.data.MissionField.Local.MISSION_DESCRIPTION
import com.upsaclay.mission.data.MissionField.Local.MISSION_DURATION
import com.upsaclay.mission.data.MissionField.Local.MISSION_END_DATE
import com.upsaclay.mission.data.MissionField.Local.MISSION_ID
import com.upsaclay.mission.data.MissionField.Local.MISSION_IMAGE_REFERENCE
import com.upsaclay.mission.data.MissionField.Local.MISSION_MANAGERS
import com.upsaclay.mission.data.MissionField.Local.MISSION_MAX_PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Local.MISSION_PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Local.MISSION_SCHOOL_LEVELS
import com.upsaclay.mission.data.MissionField.Local.MISSION_START_DATE
import com.upsaclay.mission.data.MissionField.Local.MISSION_STATE
import com.upsaclay.mission.data.MissionField.Local.MISSION_TABLE_NAME
import com.upsaclay.mission.data.MissionField.Local.MISSION_TASKS
import com.upsaclay.mission.data.MissionField.Local.MISSION_TITLE

@Entity(tableName = MISSION_TABLE_NAME)
data class LocalMission(
    @PrimaryKey
    @ColumnInfo(name = MISSION_ID)
    val missionId: String,
    @ColumnInfo(name = MISSION_TITLE)
    val missionTitle: String,
    @ColumnInfo(name = MISSION_DESCRIPTION)
    val missionDescription: String,
    @ColumnInfo(name = MISSION_SCHOOL_LEVELS)
    val missionSchoolLevels: String,
    @ColumnInfo(name = MISSION_DATE)
    val missionDate: Long,
    @ColumnInfo(name = MISSION_START_DATE)
    val missionStartDate: Long,
    @ColumnInfo(name = MISSION_END_DATE)
    val missionEndDate: Long,
    @ColumnInfo(name = MISSION_DURATION)
    val missionDuration: String?,
    @ColumnInfo(name = MISSION_MANAGERS)
    val missionManagers: String,
    @ColumnInfo(name = MISSION_PARTICIPANTS)
    val missionParticipants: String?,
    @ColumnInfo(name = MISSION_MAX_PARTICIPANTS)
    val missionMaxParticipants: Int,
    @ColumnInfo(name = MISSION_TASKS)
    val missionTasks: String?,
    @ColumnInfo(name = MISSION_IMAGE_REFERENCE)
    val missionImageFileName: String?,
    @ColumnInfo(name = MISSION_STATE)
    val missionState: String
)
