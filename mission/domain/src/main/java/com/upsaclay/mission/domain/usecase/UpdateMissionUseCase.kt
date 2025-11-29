package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.MissionUtils
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository

class UpdateMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(mission: Mission, newImageUri: String?, oldMissionState: MissionState) {
        val fileName = MissionUtils.formatImageFileName(mission.id)

        val newImage = newImageUri?.let {
            imageRepository.createCacheImage(fileName, it)
        }

        val newMission = newImage?.let {
            mission.copy(state = MissionState.Published(it.name))
        } ?: mission

        missionRepository.updateMission(newMission, newImage)
        deleteUnusedImages(newImage?.name, newMission.state, oldMissionState)
    }

    private suspend fun deleteUnusedImages(
        newImageName: String?,
        newState: MissionState,
        oldState: MissionState
    ) {
        newImageName?.let { imageRepository.deleteCacheImage(it) }

        (oldState as? MissionState.Published)
            ?.takeIf { it.imageReference != newState.imageReference }
            ?.imageUrl?.let {
                imageRepository.deleteRemoteImage(it)
            }
    }
}