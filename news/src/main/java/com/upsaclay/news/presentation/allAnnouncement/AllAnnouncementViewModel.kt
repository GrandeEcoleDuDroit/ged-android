package com.upsaclay.news.presentation.allAnnouncement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.news.R
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllAnnouncementViewModel(
    private val refreshAnnouncementUseCase: RefreshAnnouncementUseCase,
    private val userRepository: UserRepository,
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AllAnnouncementUiState())
    val uiState: StateFlow<AllAnnouncementUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        synchronizeAnnouncements()
        initUiState()
    }

    private fun initUiState() {
        combine(
            userRepository.user,
            announcementRepository.announcements
        ) { user, announcements ->
            _uiState.update {
                AllAnnouncementUiState(
                    user = user,
                    announcements = announcements.map {
                        it.copy(
                            title = it.title?.takeIf { it.isNotBlank() }?.take(100),
                            content = it.content.take(100)
                        )
                    }
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun synchronizeAnnouncements() {
        viewModelScope.launch {
            try {
                refreshAnnouncementUseCase()
            } catch (e: Exception) {
                delay(500)
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            }
        }
    }

    fun refreshAnnouncements(){
        viewModelScope.launch {
            _uiState.update { it.copy(refreshing = true) }
            try {
                refreshAnnouncementUseCase()
                _uiState.update { it.copy(refreshing = false) }
            } catch (e: Exception) {
                delay(500)
                _uiState.update { it.copy(refreshing = false) }
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            }
        }
    }
    private fun mapErrorMessage(e: Exception): Int {
        return when (e) {
            is NoInternetConnectionException -> com.upsaclay.common.R.string.no_internet_connection
            else -> R.string.announcement_refresh_error
        }
    }

    data class AllAnnouncementUiState(
        val user: User? = null,
        val announcements: List<Announcement>? = null,
        val refreshing: Boolean = false
    )
}