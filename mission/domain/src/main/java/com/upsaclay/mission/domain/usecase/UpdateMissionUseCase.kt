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
    suspend operator fun invoke(mission: Mission, imageUri: String?, previousMissionState: MissionState) {
        var newImagePath: String? = null

        val newImageFile = imageUri?.let { uri ->
            val extension = imageRepository.getFileExtension(uri)
            val fileName = "${MissionUtils.Image.generateFileName(mission.id)}.$extension"
            newImagePath = MissionUtils.Image.makeRelativePath(fileName)
            imageRepository.createCacheImage(newImagePath!!, uri)
        }

        val missionToUpdate = newImagePath?.let {
            mission.copy(state = MissionState.Published(it))
        } ?: mission

        missionRepository.updateMission(missionToUpdate, newImageFile)
        runCatching {
            deleteUnusedImages(newImagePath, missionToUpdate.state, previousMissionState)
        }
    }

    private suspend fun deleteUnusedImages(
        newImagePath: String?,
        missionState: MissionState,
        previousMissionState: MissionState
    ) {
        newImagePath?.let {
            imageRepository.deleteCacheImage(it)
        }

        val previousImageUrl = (previousMissionState as? MissionState.Published)
            ?.takeIf { it.imageReference != missionState.imageReference }
            ?.imageUrl


        MissionUtils.Image.getPath(previousImageUrl)?.let {
            imageRepository.deleteRemoteImage(it)
        }
    }
}