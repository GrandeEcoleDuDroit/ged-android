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
        var missionToUpdate = mission
        val missionSchoolLevelSet = mission.schoolLevels.map { it.number }.toSet()

        val newImageFile = imageUri?.let { uri ->
            val extension = imageRepository.getFileExtension(uri)
            val fileName = "${MissionUtils.Image.generateFileName(mission.id)}.$extension"
            newImagePath = MissionUtils.Image.makeRelativePath(fileName)
            imageRepository.createCacheImage(newImagePath!!, uri)
        }

        newImagePath?.let {
            missionToUpdate = missionToUpdate.copy(state = MissionState.Published(it))
        }
        val newParticipants = missionToUpdate.participants.filter {
            missionSchoolLevelSet.contains(it.schoolLevel.number)
        }
        missionToUpdate = missionToUpdate.copy(participants = newParticipants)

        missionRepository.updateMission(user, missionToUpdate, newImageFile)

        newImagePath?.let {
            imageRepository.deleteCacheImage(it)
        }
    }
}