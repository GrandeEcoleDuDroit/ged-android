package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.MissionJobQueue
import com.upsaclay.mission.domain.MissionUtils
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateMissionUseCase(
    private val missionRepository: MissionRepository,
    private val imageRepository: ImageRepository,
    private val missionJobQueue: MissionJobQueue,
    private val scope: CoroutineScope
) {
    suspend operator fun invoke(mission: Mission, imageUri: String?) {
        val job = scope.launch {
            var imagePath: String? = null
            val imageFile = imageUri?.let { uri ->
                val extension = imageRepository.getFileExtension(uri)
                val fileName = "${MissionUtils.Image.generateFileName(mission.id)}.$extension"
                imagePath = MissionUtils.Image.makeRelativePath(fileName)
                imageRepository.createLocalImage(imagePath!!, uri)
            }

            try {
                missionRepository.createMission(
                    mission.copy(state = MissionState.Publishing(imagePath)),
                    imageFile
                )

                missionRepository.upsertLocalMission(
                    mission.copy(state = MissionState.Published(imagePath))
                )

                imagePath?.let {
                    imageRepository.deleteLocalImage(it)
                }
                missionJobQueue.cancelAndRemoveJob(mission.id)
            } catch (e: Exception) {
                missionRepository.upsertLocalMission(
                    mission.copy(state = MissionState.Error(imagePath))
                )
                missionJobQueue.cancelAndRemoveJob(mission.id)
            }
        }

        missionJobQueue.addJob(job, mission.id)
    }
}