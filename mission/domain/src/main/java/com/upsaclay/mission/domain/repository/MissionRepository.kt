package com.upsaclay.mission.domain.repository

import com.upsaclay.mission.domain.entity.AddMissionParticipant
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MissionRepository {
    val missions: Flow<List<Mission>>

    val currentMissions: List<Mission>

    fun getMissionFlow(missionId: String): Flow<Mission>

    suspend fun getRemoteMissions(): List<Mission>

    suspend fun createMission(mission: Mission, imageFile: File?)

    suspend fun updateMission(mission: Mission, imageFile: File?)

    suspend fun upsertLocalMission(mission: Mission)

    suspend fun deleteMission(mission: Mission, imageUrl: String?)

    suspend fun deleteLocalMission(mission: Mission)

    suspend fun reportMission(report: MissionReport)

    suspend fun addParticipant(addMissionParticipant: AddMissionParticipant)

    suspend fun removeParticipant(missionId: String, userId: String)
}