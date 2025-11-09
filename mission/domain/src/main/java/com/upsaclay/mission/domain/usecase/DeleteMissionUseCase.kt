package com.upsaclay.mission.domain.usecase

import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository

class DeleteMissionUseCase(
    private val missionRepository: MissionRepository
) {
    suspend operator fun invoke(mission: Mission) {
        if (mission.state is MissionState.Published) {
            missionRepository.deleteMission(mission, mission.state.imageUrl)
        } else {
            missionRepository.deleteLocalMission(mission)
        }
    }
}