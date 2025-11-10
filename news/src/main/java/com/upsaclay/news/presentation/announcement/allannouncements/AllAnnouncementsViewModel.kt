package com.upsaclay.news.presentation.announcement.allannouncements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.news.R
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementsUseCase
import com.upsaclay.news.domain.usecase.ResendAnnouncementUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllAnnouncementsViewModel(
    private val userRepository: UserRepository,
    private val announcementRepository: AnnouncementRepository,
    private val refreshAnnouncementsUseCase: RefreshAnnouncementsUseCase,
    private val resendAnnouncementUseCase: ResendAnnouncementUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase,
    private val connectivityObserver: ConnectivityObserver
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
        _uiState.update {
            it.copy(refreshing = true)
        }
        viewModelScope.launch {
            try {
                refreshAnnouncementsUseCase()
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(refreshing = false) }
            }
        }
    }

    fun resendAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            resendAnnouncementUseCase(announcement)
        }
    }

    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }
                _uiState.update {
                    it.copy(loading = true)
                }
                deleteAnnouncementUseCase(announcement)
                _event.emit(SingleUiEvent.Success(R.string.announcement_deleted))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    fun reportAnnouncement(report: AnnouncementReport) {
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }
                _uiState.update { it.copy(loading = true) }
                announcementRepository.reportAnnouncement(report)
                _event.emit(SingleUiEvent.Success(R.string.announcement_reported))
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
                    it.copy(announcements = announcements)
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
            else -> R.string.announcements_refresh_error
        }
    }

    data class AllAnnouncementsUiState(
        val announcements: List<Announcement>? = null,
        val user: User? = null,
        val loading: Boolean = false,
        val refreshing: Boolean = false
    )
}