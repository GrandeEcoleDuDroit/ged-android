package com.upsaclay.mission.domain.repository

import com.upsaclay.mission.domain.entity.Mission
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MissionRepository {
    val missions: Flow<List<Mission>>

    fun getMissionFlow(missionId: Int): Flow<Mission>

    suspend fun createMission(mission: Mission, file: File?)

    suspend fun upsertLocalMission(mission: Mission)

    suspend fun deleteMission(mission: Mission, imageUrl: String?)

    suspend fun deleteLocalMission(mission: Mission)
}