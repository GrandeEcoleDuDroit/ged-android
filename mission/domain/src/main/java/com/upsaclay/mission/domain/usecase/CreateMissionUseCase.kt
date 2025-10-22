package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateMissionUseCase(
    private val missionRepository: MissionRepository,
    private val fileRepository: FileRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(mission: Mission) {
        val fileName = formatImageFileName(mission.id.toString())

        scope.launch {
            val imageFile = mission.state.let { state ->
                when (state) {
                    is MissionState.Draft -> {
                        state.imageUri?.let { uri ->
                            fileRepository.createLocalFile(fileName, uri)
                        }
                    }

                    else -> null
                }
            }

            try {
                missionRepository.createMission(
                    mission.copy(state = MissionState.Publishing(imageFile?.path)),
                    imageFile
                )
                missionRepository.upsertLocalMission(
                    mission.copy(state = MissionState.Published(imageFile?.name))
                )
            } catch (e: Exception) {
                missionRepository.upsertLocalMission(
                    mission.copy(state = MissionState.Error(imageFile?.path))
                )
            }
        }
    }

    private fun formatImageFileName(missionId: String): String =
        "${missionId}-mission-image-${System.currentTimeMillis()}"
}