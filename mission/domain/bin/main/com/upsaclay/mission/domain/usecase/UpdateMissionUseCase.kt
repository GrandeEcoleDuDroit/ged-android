package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.MissionUtils
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository

class UpdateMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository
) {
    suspend fun execute(user: User,  mission: Mission, imageUri: String?) {
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

        missionRepository.updateMission(user, missionToUpdate, newImageFile)

        newImagePath?.let {
            imageRepository.deleteCacheImage(it)
        }
    }
}