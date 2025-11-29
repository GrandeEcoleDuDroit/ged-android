package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.MissionUtils
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ResendMissionUseCase(
    private val missionRepository: MissionRepository,
    private val fileRepository: FileRepository,
    private val imageRepository: ImageRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(mission: Mission) {
        if (mission.state is MissionState.Error) {
            scope.launch {
                val imageFile = mission.state.imagePath?.let { path ->
                    fileRepository.getFile(path)
                }

                try {
                    missionRepository.createMission(
                        mission.copy(state = MissionState.Publishing(mission.state.imagePath)),
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
}