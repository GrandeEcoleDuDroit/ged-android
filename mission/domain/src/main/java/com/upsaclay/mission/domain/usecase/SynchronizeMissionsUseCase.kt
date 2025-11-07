package com.upsaclay.mission.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.mission.domain.repository.MissionRepository

class SynchronizeMissionsUseCase(
    private val missionRepository: MissionRepository,
    private val blockedUserRepository: BlockedUserRepository
) {
    suspend operator fun invoke() {
        val missions = missionRepository.currentMissions
        val remoteMissions = missionRepository.getRemoteMissions()
    }
}