package com.upsaclay.mission.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.upsaclay.mission.data.MissionField.Local.MISSION_DATE
import com.upsaclay.mission.data.MissionField.Local.MISSION_ID
import com.upsaclay.mission.data.MissionField.Local.MISSION_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionDao {
    @Query("SELECT * FROM $MISSION_TABLE_NAME ORDER BY $MISSION_DATE DESC")
    fun getMissions(): Flow<List<LocalMission>>

    @Query("SELECT * FROM $MISSION_TABLE_NAME WHERE $MISSION_ID = :missionId")
    fun getMissionFlow(missionId: String): Flow<LocalMission?>

    @Query("SELECT * FROM $MISSION_TABLE_NAME WHERE $MISSION_ID = :missionId")
    fun getMission(missionId: String): LocalMission?

    @Upsert
    suspend fun upsertMission(mission: LocalMission)

    @Delete
    suspend fun deleteMission(mission: LocalMission)
}