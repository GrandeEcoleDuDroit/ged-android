package com.upsaclay.mission.presentation.missiondetails

import androidx.annotation.StringRes
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
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.DeleteMissionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissionDetailsViewModel(
    private val missionId: Long,
    private val missionRepository: MissionRepository,
    private val userRepository: UserRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val deleteMissionUseCase: DeleteMissionUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(MissionDetailsUiState())
    val uiState: StateFlow<MissionDetailsUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenUser()
        listenMission()
    }

    fun registerToMission() {
        TODO("Not yet implemented")
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
                _event.emit(MissionDetailsUiEvent.MissionReported(R.string.mission_reported))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    fun deleteMission() {
        val mission = uiState.value.mission ?: return
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }
                _uiState.update {
                    it.copy(loading = true)
                }
                deleteMissionUseCase(mission)
                _event.emit(MissionDetailsUiEvent.MissionDeleted)
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun listenMission() {
        viewModelScope.launch {
            missionRepository.getMissionFlow(missionId).collect { mission ->
                _uiState.update {
                    it.copy(
                        mission = mission,
                        registrationDisabled = invalidRegistration(it.user, mission)
                    )
                }
            }
        }
    }

    private fun listenUser() {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update {
                    it.copy(
                        user = user,
                        registrationDisabled = invalidRegistration(user, it.mission)
                    )
                }
            }
        }
    }

    private fun invalidRegistration(user: User?, mission: Mission?): Boolean {
        if (user == null || mission == null) return true

        return mission.full || mission.expired ||
                mission.schoolLevelPermitted(user.schoolLevel)
    }

    data class MissionDetailsUiState(
        val user: User? = null,
        val mission: Mission? = null,
        val loading: Boolean = false,
        val registrationDisabled: Boolean? = null
    )

    sealed interface MissionDetailsUiEvent : SingleUiEvent {
        data object MissionDeleted : MissionDetailsUiEvent
        data class MissionReported(@StringRes val messageId: Int) : MissionDetailsUiEvent
    }
}