package com.upsaclay.news.presentation.announcement.readannouncement

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.news.R
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
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
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenAnnouncement()
        listenUser()
    }

    fun reportAnnouncement(report: AnnouncementReport) {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                announcementRepository.reportAnnouncement(report)
                _event.emit(ReadAnnouncementUiEvent.AnnouncementReported(R.string.announcement_reported))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun deleteAnnouncement() {
        val announcement = uiState.value.announcement ?: return
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                deleteAnnouncementUseCase(announcement)
                _event.emit(ReadAnnouncementUiEvent.AnnouncementDeleted)
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
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

    sealed interface ReadAnnouncementUiEvent: SingleUiEvent {
        data object AnnouncementDeleted: ReadAnnouncementUiEvent
        data class AnnouncementReported(@StringRes val messageId: Int): ReadAnnouncementUiEvent
    }
}