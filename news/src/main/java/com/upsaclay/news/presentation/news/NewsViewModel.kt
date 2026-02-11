package com.upsaclay.news.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.AnnouncementReport
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import com.upsaclay.news.domain.announcement.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.announcement.usecase.RecreateAnnouncementUseCase
import com.upsaclay.news.domain.announcement.usecase.RefreshAnnouncementsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsViewModel(
    private val recreateAnnouncementUseCase: RecreateAnnouncementUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase,
    private val refreshAnnouncementsUseCase: RefreshAnnouncementsUseCase,
    private val announcementRepository: AnnouncementRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenAnnouncements()
        listenUser()
    }

    fun getAnnouncement(announcementId: String): Announcement? =
        announcementRepository.getAnnouncement(announcementId)

    fun refreshAnnouncements() {
        viewModelScope.executeUiBlockingRequest(
            block = { refreshAnnouncementsUseCase.execute() },
            onLoading = {
                _uiState.update { it.copy(refreshing = true) }
            },
            onError = {
                _event.emit(SingleUiEvent.Error(R.string.announcements_refresh_error))
            },
            onFinished = {
                _uiState.update { it.copy(refreshing = false) }
            }
        )
    }

    fun resendAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            recreateAnnouncementUseCase.execute(announcement)
        }
    }

    fun deleteAnnouncement(announcement: Announcement) {
        executeRequest {
            deleteAnnouncementUseCase.execute(announcement)
            _event.emit(SingleUiEvent.Success(R.string.announcement_deleted))
        }
    }

    fun reportAnnouncement(report: AnnouncementReport) {
        executeRequest {
            announcementRepository.reportAnnouncement(report)
            _event.emit(SingleUiEvent.Success(R.string.announcement_reported))
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

    private fun listenAnnouncements() {
        viewModelScope.launch {
            announcementRepository.announcements.collect { announcements ->
                _uiState.update { state ->
                    state.copy(
                        announcements = announcements.map {
                            it.copy(
                                title = it.title?.takeIf { it.isNotBlank() }?.take(100),
                                content = it.content.take(100)
                            )
                        }
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

    data class NewsUiState(
        val user: User? = null,
        val announcements: List<Announcement>? = null,
        val refreshing: Boolean = false,
        val loading: Boolean = false
    )
}