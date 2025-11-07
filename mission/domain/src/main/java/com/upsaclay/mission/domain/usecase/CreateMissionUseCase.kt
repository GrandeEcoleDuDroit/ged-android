package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(mission: Mission) {
        val fileName = imageFileName(mission.id.toString())

        scope.launch {
            val image = mission.state.let { state ->
                when (state) {
                    is MissionState.Draft -> {
                        state.imageUri?.let { uri ->
                            imageRepository.createLocalImage(fileName, uri)
                        }
                    }

                    else -> null
                }
            }

            try {
                missionRepository.createMission(
                    mission.copy(state = MissionState.Publishing(image?.path)),
                    image
                )
                missionRepository.upsertLocalMission(
                    mission.copy(state = MissionState.Published(image?.name))
                )
            } catch (e: Exception) {
                missionRepository.upsertLocalMission(
                    mission.copy(state = MissionState.Error(image?.path))
                )
            }
        }
    }

    private fun imageFileName(missionId: String): String = "${missionId}-mission-image-${System.currentTimeMillis()}"
}