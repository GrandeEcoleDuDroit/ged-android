package com.upsaclay.mission.domain.usecase

import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository

class FetchMissionsUseCase(
    private val missionRepository: MissionRepository,
    private val upsertLocalMissionUseCase: UpsertLocalMissionUseCase
) {
    suspend fun execute() {
        val missions = missionRepository.currentMissions
        val remoteMissions = missionRepository.getRemoteMissions()

        val missionsToDelete = missions.filter { (it.state is MissionState.Published && it !in remoteMissions) }
        val missionsToUpsert = remoteMissions.filter { it !in missions }

        missionsToDelete.forEach { missionRepository.deleteLocalMission(it) }
        missionsToUpsert.forEach { upsertLocalMissionUseCase.execute(it) }
    }
}