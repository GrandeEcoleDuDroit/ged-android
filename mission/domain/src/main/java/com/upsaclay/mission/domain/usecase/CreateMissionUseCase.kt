package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.MissionUtils
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(mission: Mission, imageUri: String?) {
        scope.launch {
            val fileName = MissionUtils.formatImageFileName(mission.id)
            val imageFile = imageUri?.let { uri ->
                val extension = imageRepository.getFileExtension(uri)
                imageRepository.createLocalImage(
                    MissionUtils.FOLDER_NAME,
                    "$fileName.$extension",
                    uri
                )
            }

            try {
                missionRepository.createMission(
                    mission.copy(state = MissionState.Publishing(imageFile?.path)),
                    imageFile
                )
                missionRepository.upsertLocalMission(
                    mission.copy(state = MissionState.Published(imageFile?.name))
                )
                imageFile?.name?.let {
                    imageRepository.deleteLocalImage(MissionUtils.FOLDER_NAME, it)
                }
            } catch (e: Exception) {
                missionRepository.upsertLocalMission(
                    mission.copy(state = MissionState.Error(imageFile?.path))
                )
            }
        }
    }
}