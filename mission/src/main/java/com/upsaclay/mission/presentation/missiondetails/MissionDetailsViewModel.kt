package com.upsaclay.mission.presentation.missiondetails

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.launchDelayed
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.mission.R
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
    private val deleteMissionUseCase: DeleteMissionUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(MissionDetailsUiState())
    val uiState: StateFlow<MissionDetailsUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenUserAndMission()
    }

    fun registerToMission() {
        val currentUser = uiState.value.currentUser ?: return
        executeRequest {
            missionRepository.addParticipant(missionId, currentUser)
        }
    }

    fun unregisterFromMission() {
        val user = uiState.value.currentUser ?: return
        executeRequest {
            missionRepository.removeParticipant(missionId, user.id)
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

    fun reportMission(report: MissionReport) {
        executeRequest {
            missionRepository.reportMission(report)
            _event.emit(SingleUiEvent.Success(R.string.mission_reported))
        }
    }

    private fun executeRequest(block: suspend () -> Unit) {
        var loadingJob: Job? = null

        viewModelScope.launch {
            try {
                loadingJob = launchDelayed(300) {
                    _uiState.update { it.copy(loading = true) }
                }

                block()
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(e)))
            } finally {
                loadingJob?.cancel()
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun listenUserAndMission() {
        combine(
            userRepository.user,
            missionRepository.getMissionFlow(missionId)
        ) { user, mission ->
            val isManager = mission.managers.any { it.id == user.id }
            _uiState.update {
                it.copy(
                    currentUser = user,
                    mission = mission,
                    isManager = isManager,
                    buttonState = updateButtonState(user, mission, isManager)
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updateButtonState(user: User, mission: Mission, isManager: Boolean): MissionButtonState {
        return when {
            isManager -> MissionButtonState.Hidden

            mission.completed -> MissionButtonState.Completed

            mission.participants.any { it.id == user.id } -> MissionButtonState.Registered

            !mission.schoolLevels.contains(user.schoolLevel) ->
                MissionButtonState.Unavailable(R.string.non_matching_school_level_information_text)

            mission.full -> MissionButtonState.RegistrationClosed(R.string.full_mission_information_text)

            else -> MissionButtonState.Register
        }
    }

    data class MissionDetailsUiState(
        val currentUser: User? = null,
        val mission: Mission? = null,
        val isManager: Boolean = false,
        val loading: Boolean = false,
        val buttonState: MissionButtonState = MissionButtonState.Hidden
    )

    sealed class MissionDetailsUiEvent: SingleUiEvent {
        data object MissionDetailsDeleted: MissionDetailsUiEvent()
    }

    sealed class MissionButtonState {
        data object Register: MissionButtonState()
        data object Registered: MissionButtonState()
        data object Completed: MissionButtonState()
        data class RegistrationClosed(@StringRes val reason: Int): MissionButtonState()
        data class Unavailable(@StringRes val reason: Int): MissionButtonState()
        data object Hidden: MissionButtonState()
    }
}