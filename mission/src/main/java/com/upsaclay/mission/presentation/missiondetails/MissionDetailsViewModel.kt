package com.upsaclay.mission.presentation.missiondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.launchDelayed
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.AddMissionParticipant
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.DeleteMissionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MissionDetailsViewModel(
    private val missionId: String,
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
        listenUserMission()
    }

    fun registerToMission() {
        val user = uiState.value.user ?: return
        val mission = uiState.value.mission ?: return
        val addMissionParticipant = AddMissionParticipant(
            missionId = missionId,
            schoolLevels = mission.schoolLevels,
            maxParticipants = mission.maxParticipants,
            participantsNumber = mission.participants.size,
            user = user
        )
       executeRequest {
           missionRepository.addParticipant(addMissionParticipant)
       }
    }

    fun unregisterFromMission() {
        val user = uiState.value.user ?: return
        executeRequest {
            missionRepository.removeParticipant(missionId, user.id)
        }
    }

    fun reportMission(report: MissionReport) {
        executeRequest {
            missionRepository.reportMission(report)
            _event.emit(SingleUiEvent.Success(R.string.mission_reported))
        }
    }

    fun deleteMission() {
        val mission = uiState.value.mission ?: return
        executeRequest {
            deleteMissionUseCase(mission)
            _event.emit(MissionDetailsUiEvent.MissionDetailsDeleted)
        }
    }

    fun removeParticipant(userId: String) {
        val missionId = uiState.value.mission?.id ?: return
        executeRequest {
            missionRepository.removeParticipant(missionId, userId)
        }
    }

    private fun executeRequest(block: suspend () -> Unit) {
        var loadingJob: Job? = null

        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }

                loadingJob = launchDelayed(300) {
                    _uiState.update { it.copy(loading = true) }
                }

                block()
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                loadingJob?.cancel()
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun listenUserMission() {
        combine(
            userRepository.user,
            missionRepository.getMissionFlow(missionId)
        ) { user, mission ->
            val isManager = mission.managers.any { it.id == user.id }
            _uiState.update {
                it.copy(
                    user = user,
                    mission = mission,
                    isManager = isManager,
                    buttonState = updateMissionButtonState(user, mission, isManager)
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updateMissionButtonState(
        user: User,
        mission: Mission,
        isManager: Boolean
    ): MissionButtonState {
        return when {
            isManager -> MissionButtonState.Hidden

            mission.complete -> MissionButtonState.Complete

            mission.participants.any { it.id == user.id } -> MissionButtonState.Registered

            else -> {
                val enabled = !mission.full && mission.schoolLevelPermitted(user.schoolLevel)
                MissionButtonState.Register(enabled)
            }
        }
    }

    data class MissionDetailsUiState(
        val user: User? = null,
        val mission: Mission? = null,
        val isManager: Boolean = false,
        val loading: Boolean = false,
        val buttonState: MissionButtonState = MissionButtonState.Hidden
    )

    sealed class MissionDetailsUiEvent: SingleUiEvent {
        data object MissionDetailsDeleted: MissionDetailsUiEvent()
    }

    sealed class MissionButtonState {
        data class Register(val enabled: Boolean = true): MissionButtonState()
        data object Registered: MissionButtonState()
        data object Complete: MissionButtonState()
        data object Hidden: MissionButtonState()
    }
}