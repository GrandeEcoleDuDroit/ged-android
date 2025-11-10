package com.upsaclay.mission.data.repositories

import com.upsaclay.common.data.UrlUtils
import com.upsaclay.mission.data.local.MissionLocalDataSource
import com.upsaclay.mission.data.remote.MissionRemoteDataSource
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
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

    override suspend fun getRemoteMissions(): List<Mission> = missionRemoteDataSource.getMissions()

    override suspend fun createMission(mission: Mission, imageFile: File?) {
        missionLocalDataSource.upsertMission(mission)
        missionRemoteDataSource.createMission(mission, imageFile)
    }

    override suspend fun updateMission(mission: Mission, imageFile: File?) {
        missionRemoteDataSource.updateMission(mission, imageFile)
        missionLocalDataSource.upsertMission(mission)
    }

    override suspend fun upsertLocalMission(mission: Mission) {
        missionLocalDataSource.upsertMission(mission)
    }

    override suspend fun deleteMission(mission: Mission, imageUrl: String?) {
        val imageFileName = UrlUtils.extractFileName(imageUrl)
        missionRemoteDataSource.deleteMission(mission.id, imageFileName)
        missionLocalDataSource.deleteMission(mission)
    }

    override suspend fun deleteLocalMission(mission: Mission) {
        missionLocalDataSource.deleteMission(mission)
    }
}