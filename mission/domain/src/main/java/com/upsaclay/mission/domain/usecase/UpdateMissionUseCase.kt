package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.MissionUtils.imageFileName
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository

class UpdateMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(mission: Mission, newImageUri: String?, oldMissionState: MissionState) {
        val fileName = imageFileName(mission.id.toString())
        val newImage = newImageUri?.let {
            imageRepository.createLocalImage(fileName, it)
        }
        val newMission = newImage?.let {
            mission.copy(state = MissionState.Published(it.name))
        } ?: mission

        missionRepository.updateMission(newMission, newImage)
        deleteUnusedImages(newImage?.name, newMission.state, oldMissionState)
    }

    private suspend fun deleteUnusedImages(
        newImageName: String?,
        newMissionState: MissionState,
        oldMissionState: MissionState
    ) {
        newImageName?.let { name ->
            imageRepository.deleteLocalImage(name)
        }

        (oldMissionState as? MissionState.Published)
            ?.takeIf { newMissionState != it }
            ?.imageUrl
            ?.let {
                imageRepository.deleteRemoteImage(it)
            }
    }
}