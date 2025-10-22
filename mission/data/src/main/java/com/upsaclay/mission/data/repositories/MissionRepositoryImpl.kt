package com.upsaclay.mission.data.repositories

import com.upsaclay.mission.data.local.MissionLocalDataSource
import com.upsaclay.mission.data.remote.MissionRemoteDataSource
import com.upsaclay.mission.domain.entity.Mission
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

    override suspend fun createMission(mission: Mission, file: File?) {
        missionLocalDataSource.upsertMission(mission)
        missionRemoteDataSource.createMission(mission, file)
    }

    override suspend fun upsertLocalMission(mission: Mission) {
        missionLocalDataSource.upsertMission(mission)
    }

    override suspend fun deleteMission(mission: Mission, imageUrl: String?) {
        missionRemoteDataSource.deleteMission(mission.id, imageUrl)
        missionLocalDataSource.deleteMission(mission)
    }

    override suspend fun deleteLocalMission(mission: Mission) {
        missionLocalDataSource.deleteMission(mission)
    }
}