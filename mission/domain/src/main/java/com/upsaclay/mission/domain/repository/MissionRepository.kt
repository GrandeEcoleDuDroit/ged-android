package com.upsaclay.mission.domain.repository

import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MissionRepository {
    val missions: Flow<List<Mission>>

    fun getMissionFlow(missionId: String): Flow<Mission>

    suspend fun getRemoteMissions(): List<Mission>

    suspend fun getLocalMissions(): List<Mission>

    suspend fun getLocalMission(missionId: String): Mission?

    suspend fun createMission(mission: Mission, imageFile: File?)

    suspend fun updateMission(user: User, mission: Mission, imageFile: File?)

    suspend fun upsertLocalMission(mission: Mission)

    suspend fun deleteMission(mission: Mission, imageUrl: String?)

    suspend fun deleteLocalMission(mission: Mission)

    suspend fun reportMission(report: MissionReport)

    suspend fun addParticipant(missionId: String, user: User)

    suspend fun removeParticipant(missionId: String, userId: String)
}