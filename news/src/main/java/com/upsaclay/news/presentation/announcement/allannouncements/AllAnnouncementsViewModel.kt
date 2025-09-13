package com.upsaclay.news.presentation.announcement.allannouncements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.news.R
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import com.upsaclay.news.domain.usecase.ResendAnnouncementUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllAnnouncementsViewModel(
    private val userRepository: UserRepository,
    private val announcementRepository: AnnouncementRepository,
    private val refreshAnnouncementUseCase: RefreshAnnouncementUseCase,
    private val resendAnnouncementUseCase: ResendAnnouncementUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AllAnnouncementsUiState())
    val uiState: StateFlow<AllAnnouncementsUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenAnnouncements()
        listenUser()
    }

    fun refreshAnnouncements() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(refreshing = true)
            }

            try {
                refreshAnnouncementUseCase()
                delay(500)
                _uiState.update {
                    it.copy(refreshing = false)
                }
            } catch (e: Exception) {
                delay(500)
                _uiState.update {
                    it.copy(refreshing = false)
                }
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            }
        }
    }

    fun resendAnnouncement(announcement: Announcement) {
        resendAnnouncementUseCase(announcement)
    }

    fun deleteAnnouncement(announcement: Announcement) {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                deleteAnnouncementUseCase(announcement)
                _event.emit(SingleUiEvent.Success(R.string.announcement_deleted))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun listenAnnouncements() {
        viewModelScope.launch {
            announcementRepository.announcements.collect { announcements ->
                _uiState.update {
                    it.copy(
                        announcements = announcements,
                    )
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

    private fun mapErrorMessage(e: Exception): Int {
        return when (e) {
            is NoInternetConnectionException -> com.upsaclay.common.R.string.no_internet_connection
            else -> R.string.announcement_refresh_error
        }
    }

    data class AllAnnouncementsUiState(
        val announcements: List<Announcement>? = null,
        val user: User? = null,
        val loading: Boolean = false,
        val refreshing: Boolean = false
    )
}