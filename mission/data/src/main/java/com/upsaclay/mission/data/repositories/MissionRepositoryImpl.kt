package com.upsaclay.mission.data.repositories

import com.upsaclay.common.data.utils.e
import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.data.local.MissionLocalDataSource
import com.upsaclay.mission.data.remote.MissionRemoteDataSource
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.io.File

class MissionRepositoryImpl(
    private val missionLocalDataSource: MissionLocalDataSource,
    private val missionRemoteDataSource: MissionRemoteDataSource,
    scope: CoroutineScope
): MissionRepository {
    private val _missions = missionLocalDataSource.getMissions()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    override val missions: Flow<List<Mission>> = _missions

    override val currentMissions: List<Mission>
        get() = _missions.value

    override fun getMissionFlow(missionId: String): Flow<Mission> =
        missionLocalDataSource.getMissionFlow(missionId)

    override suspend fun getRemoteMissions(): List<Mission> {
        return try {
            missionRemoteDataSource.getMissions()
        } catch (e: Exception) {
            e("Error getting remote missions", e)
            throw e
        }
    }

    override suspend fun createMission(mission: Mission, imageFile: File?) {
        try {
            missionLocalDataSource.upsertMission(mission)
            missionRemoteDataSource.createMission(mission, imageFile)
        } catch (e: Exception) {
            e("Error creating mission ${mission.id}", e)
            throw e
        }
    }

    override suspend fun updateMission(user: User, mission: Mission, imageFile: File?) {
        try {
            missionRemoteDataSource.updateMission(user, mission, imageFile)
            missionLocalDataSource.upsertMission(mission)
        } catch (e: Exception) {
            e("Error updating mission ${mission.id}", e)
            throw e
        }
    }

    override suspend fun upsertLocalMission(mission: Mission) {
        missionLocalDataSource.upsertMission(mission)
    }

    override suspend fun deleteMission(mission: Mission, imageUrl: String?) {
        try {
            missionRemoteDataSource.deleteMission(mission)
            missionLocalDataSource.deleteMission(mission)
        } catch (e: Exception) {
            e("Error deleting mission ${mission.id}", e)
            throw e
        }
    }

    override suspend fun deleteLocalMission(mission: Mission) {
        missionLocalDataSource.deleteMission(mission)
    }

    override suspend fun reportMission(report: MissionReport) {
        try {
            missionRemoteDataSource.reportMission(report)
        } catch (e: Exception) {
            e("Error reporting mission ${report.missionId}", e)
            throw e
        }
    }

    override suspend fun addParticipant(missionId: String, user: User) {
        try {
            missionRemoteDataSource.addParticipant(missionId, user)
            missionLocalDataSource.addParticipant(missionId, user)
        } catch (e: Exception) {
            e("Error adding participant to mission $missionId", e)
            throw e
        }
    }

    override suspend fun removeParticipant(missionId: String, userId: String) {
        try {
            missionRemoteDataSource.removeParticipant(missionId, userId)
            missionLocalDataSource.removeParticipant(missionId, userId)
        } catch (e: Exception) {
            e("Error removing participant from mission $missionId", e)
            throw e
        }
    }
}