package com.upsaclay.news.presentation.announcement.readannouncement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReadAnnouncementViewModel(
    private val announcementId: String,
    private val userRepository: UserRepository,
    private val announcementRepository: AnnouncementRepository,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadAnnouncementUiState())
    val uiState: StateFlow<ReadAnnouncementUiState> = _uiState

    private val _singleUiEvent = MutableSharedFlow<SingleUiEvent>()
    val singleUiEvent: SharedFlow<SingleUiEvent> = _singleUiEvent

    init {
        listenAnnouncement()
        listenUser()
    }

    fun deleteAnnouncement() {
        val announcement = uiState.value.announcement ?: return
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                deleteAnnouncementUseCase(announcement)
                _singleUiEvent.emit(SingleUiEvent.Success())
            } catch (e: Exception) {
                _singleUiEvent.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun listenAnnouncement() {
        viewModelScope.launch {
            announcementRepository.getAnnouncementFlow(announcementId)
                .filterNotNull()
                .map {
                    it.copy(
                        title = it.title?.takeIf { it.isNotBlank() }
                    )
                }.collect {
                    _uiState.update { state -> state.copy(announcement = it) }
                }
        }
    }

    private fun listenUser() {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update { state -> state.copy(user = user) }
            }
        }
    }

    data class ReadAnnouncementUiState(
        val announcement: Announcement? = null,
        val user: User? = null,
        val loading: Boolean = false
    )
}