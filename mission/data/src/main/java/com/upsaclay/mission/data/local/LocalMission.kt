package com.upsaclay.mission.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.mission.data.MissionField.Local.DATE
import com.upsaclay.mission.data.MissionField.Local.DESCRIPTION
import com.upsaclay.mission.data.MissionField.Local.END_DATE
import com.upsaclay.mission.data.MissionField.Local.FREQUENCY
import com.upsaclay.mission.data.MissionField.Local.IMAGE_REFERENCE
import com.upsaclay.mission.data.MissionField.Local.MANAGERS
import com.upsaclay.mission.data.MissionField.Local.MAX_PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Local.MISSION_ID
import com.upsaclay.mission.data.MissionField.Local.PARTICIPANTS
import com.upsaclay.mission.data.MissionField.Local.SCHOOL_LEVELS
import com.upsaclay.mission.data.MissionField.Local.START_DATE
import com.upsaclay.mission.data.MissionField.Local.STATE
import com.upsaclay.mission.data.MissionField.Local.TASKS
import com.upsaclay.mission.data.MissionField.Local.TITLE

const val MISSION_TABLE = "missions_table"

@Entity(tableName = MISSION_TABLE)
data class LocalMission(
    @PrimaryKey
    @ColumnInfo(name = MISSION_ID)
    val missionId: Int,
    @ColumnInfo(name = TITLE)
    val missionTitle: String,
    @ColumnInfo(name = DESCRIPTION)
    val missionDescription: String,
    @ColumnInfo(name = SCHOOL_LEVELS)
    val missionSchoolLevels: String,
    @ColumnInfo(name = DATE)
    val missionDate: Long,
    @ColumnInfo(name = START_DATE)
    val missionStartDate: Long,
    @ColumnInfo(name = END_DATE)
    val missionEndDate: Long,
    @ColumnInfo(name = FREQUENCY)
    val missionFrequency: String,
    @ColumnInfo(name = MANAGERS)
    val missionManagers: String,
    @ColumnInfo(name = PARTICIPANTS)
    val missionParticipants: String?,
    @ColumnInfo(name = MAX_PARTICIPANTS)
    val missionMaxParticipants: Int,
    @ColumnInfo(name = TASKS)
    val missionTasks: String?,
    @ColumnInfo(name = IMAGE_REFERENCE)
    val missionImageReference: String?,
    @ColumnInfo(name = STATE)
    val missionState: String
)
