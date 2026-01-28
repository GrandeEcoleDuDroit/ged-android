package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.repository.MissionRepository

class UpsertMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository
) {
    suspend fun execute(mission: Mission) {
        val localMission = missionRepository.getLocalMission(mission.id)
        missionRepository.upsertLocalMission(mission)
        localMission?.state?.resolveImagePath()?.let {
            imageRepository.deleteLocalImage(it)
        }
    }
}