package com.upsaclay.mission.presentation.missiondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MissionDetailsViewModel(
    private val missionId: Int,
    private val missionRepository: MissionRepository,
    private val userRepository: UserRepository
): ViewModel() {
    val uiState: StateFlow<MissionDetailsUiState> =
        missionDetailsUiState()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                MissionDetailsUiState()
            )


    fun registerToMission() {
        TODO("Not yet implemented")
    }

    private fun missionDetailsUiState(): Flow<MissionDetailsUiState> = combine(
        userRepository.user,
        missionRepository.getMissionFlow(missionId)
    ) { user, mission ->
        MissionDetailsUiState(
            user = user,
            mission = mission,
            registrationDisabled = invalidRegistration(user, mission)
        )
    }

    private fun invalidRegistration(user: User, mission: Mission): Boolean =
        mission.full || mission.expired || mission.schoolLevelPermitted(user.schoolLevel)


    data class MissionDetailsUiState(
        val user: User? = null,
        val mission: Mission? = null,
        val registrationDisabled: Boolean? = null
    )
}