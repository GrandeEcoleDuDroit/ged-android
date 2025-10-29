package com.upsaclay.mission.presentation.seemission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeeMissionViewModel(
    missionId: Int,
    private val missionRepository: MissionRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SeeMissionUiState())
    val uiState: StateFlow<SeeMissionUiState> = _uiState

    init {
        listenMission(missionId)
    }

    private fun listenMission(missionId: Int) {
        viewModelScope.launch {
            missionRepository.getMissionFlow(missionId).collect { mission ->
                _uiState.update {
                    it.copy(mission = mission)
                }
            }
        }
    }

    data class SeeMissionUiState(
        val mission: Mission? = null
    )
}