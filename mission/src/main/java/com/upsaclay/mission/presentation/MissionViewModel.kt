package com.upsaclay.mission.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.DeleteMissionUseCase
import com.upsaclay.mission.domain.usecase.RecreateMissionUseCase
import com.upsaclay.mission.domain.usecase.RefreshMissionsUseCase
import com.upsaclay.mission.presentation.extension.missionSorting
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissionViewModel(
    private val missionRepository: MissionRepository,
    private val userRepository: UserRepository,
    private val recreateMissionUseCase: RecreateMissionUseCase,
    private val deleteMissionUseCase: DeleteMissionUseCase,
    private val refreshMissionsUseCase: RefreshMissionsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(MissionUiState())
    val uiState: StateFlow<MissionUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenMissions()
        listenUser()
    }

    fun refreshMissions() {
        viewModelScope.executeUiBlockingRequest(
            block = { refreshMissionsUseCase() },
            onLoading = {
                _uiState.update { it.copy(refreshing = true) }
            },
            onError = {
                _event.emit(SingleUiEvent.Error(R.string.missions_refresh_error))
            },
            onFinished = {
                _uiState.update { it.copy(refreshing = false) }
            }
        )
    }

    fun reportMission(report: MissionReport) {
        executeRequest {
            missionRepository.reportMission(report)
            _event.emit(SingleUiEvent.Success(R.string.mission_reported))
        }
    }

    fun recreateMission(mission: Mission) {
        executeRequest {
            recreateMissionUseCase(mission)
        }
    }

    fun deleteMission(mission: Mission) {
        executeRequest {
            deleteMissionUseCase(mission)
            _event.emit(SingleUiEvent.Success(R.string.mission_deleted))
        }
    }

    private fun executeRequest(block: suspend () -> Unit) {
        viewModelScope.executeUiBlockingRequest(
            block = block,
            onLoading = {
                _uiState.update { it.copy(loading = true) }
            },
            onError = {
                _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(it)))
            },
            onFinished = {
                _uiState.update { it.copy(loading = false) }
            }
        )
    }

    private fun listenMissions() {
        viewModelScope.launch {
            missionRepository.missions.collect { missions ->
                _uiState.update {
                    it.copy(missions = missions.missionSorting())
                }
            }
        }
    }

    private fun listenUser() {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update {
                    it.copy(user = user)
                }
            }
        }
    }

    data class MissionUiState(
        val missions: List<Mission>? = null,
        val user: User? = null,
        val loading: Boolean = false,
        val refreshing: Boolean = false
    )
}