package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.MissionJobQueue
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class RecreateMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository,
    private val missionJobQueue: MissionJobQueue,
    private val scope: CoroutineScope
) {
    suspend fun execute(mission: Mission) {
        if (mission.state is MissionState.Error) {
            val imagePath = mission.state.imagePath
            val job = scope.launch {
                val imageFile = imagePath?.let { path ->
                    File(path).takeIf { it.exists() }
                }

                try {
                    missionRepository.createMission(mission.copy(state = MissionState.Publishing(imagePath)), imageFile)
                    missionRepository.upsertLocalMission(mission.copy(state = MissionState.Published(imagePath)))
                    imagePath?.let {
                        imageRepository.deleteLocalImage(it)
                    }
                    missionJobQueue.cancelAndRemoveJob(mission.id)
                } catch (e: Exception) {
                    missionRepository.upsertLocalMission(mission.copy(state = MissionState.Error(imagePath)))
                    missionJobQueue.cancelAndRemoveJob(mission.id)
                }
            }

            missionJobQueue.addJob(job, mission.id)
        }
    }
}