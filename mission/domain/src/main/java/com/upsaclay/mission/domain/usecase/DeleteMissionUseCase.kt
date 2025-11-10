package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository

class DeleteMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(mission: Mission) {
        when (val state = mission.state) {
            is MissionState.Draft -> missionRepository.deleteLocalMission(mission)

            is MissionState.Publishing -> {
                missionRepository.deleteLocalMission(mission)
                state.imagePath?.let { imageRepository.deleteLocalImage(it) }
            }

            is MissionState.Published -> missionRepository.deleteMission(mission, state.imageUrl)

            is MissionState.Error -> {
                missionRepository.deleteLocalMission(mission)
                state.imagePath?.let { imageRepository.deleteLocalImage(it) }
            }
        }
    }
}