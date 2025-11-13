package com.upsaclay.mission.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.DeleteMissionUseCase
import com.upsaclay.mission.domain.usecase.RefreshMissionsUseCase
import com.upsaclay.mission.domain.usecase.ResendMissionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissionViewModel(
    private val missionRepository: MissionRepository,
    private val userRepository: UserRepository,
    private val resendMissionUseCase: ResendMissionUseCase,
    private val deleteMissionUseCase: DeleteMissionUseCase,
    private val refreshMissionsUseCase: RefreshMissionsUseCase,
    private val connectivityObserver: ConnectivityObserver
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
        _uiState.update {
            it.copy(refreshing = true)
        }
        viewModelScope.launch {
            try {
                refreshMissionsUseCase()
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapRefreshErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(refreshing = false)
                }
            }
        }
    }

    fun reportMission(report: MissionReport) {
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }
                _uiState.update {
                    it.copy(loading = true)
                }
                missionRepository.reportMission(report)
                _event.emit(SingleUiEvent.Success(R.string.mission_reported))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    fun resendMission(mission: Mission) {
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }

                _uiState.update {
                    it.copy(loading = true)
                }

                resendMissionUseCase(mission)
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    fun deleteMission(mission: Mission) {
        viewModelScope.launch {
            try {
                if (
                    !connectivityObserver.isConnected &&
                    mission.state is MissionState.Published
                ) {
                    throw NoInternetConnectionException()
                }
                _uiState.update {
                    it.copy(loading = true)
                }
                deleteMissionUseCase(mission)
                _event.emit(SingleUiEvent.Success(R.string.mission_deleted))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun listenMissions() {
        viewModelScope.launch {
            missionRepository.missions.collect { missions ->
                _uiState.update {
                    it.copy(missions = missions)
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

    private fun mapRefreshErrorMessage(e: Exception): Int {
        return when (e) {
            is NoInternetConnectionException -> com.upsaclay.common.R.string.no_internet_connection
            else -> R.string.missions_refresh_error
        }
    }

    data class MissionUiState(
        val missions: List<Mission>? = null,
        val user: User? = null,
        val loading: Boolean = false,
        val refreshing: Boolean = false
    )
}