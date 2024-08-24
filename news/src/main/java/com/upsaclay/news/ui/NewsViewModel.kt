package com.upsaclay.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetUserUseCase
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.model.AnnouncementState
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.GetAllAnnouncementsUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementsUseCase
import com.upsaclay.news.domain.usecase.UpdateAnnouncementUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    getAllAnnouncementUseCase: GetAllAnnouncementsUseCase,
    getUserUseCase: GetUserUseCase,
    private val refreshAnnouncementsUseCase: RefreshAnnouncementsUseCase,
    private val createAnnouncementUseCase: CreateAnnouncementUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase,
    private val updateAnnouncementUseCase: UpdateAnnouncementUseCase
): ViewModel() {
    private val _announcementState = MutableStateFlow(AnnouncementState.DEFAULT)
    val announcementState: Flow<AnnouncementState> = _announcementState
    val announcements: Flow<List<Announcement>> = getAllAnnouncementUseCase()
    val user: Flow<User?> = getUserUseCase()

    var displayedAnnouncement: Announcement? = null
        private set

    fun refreshAnnouncements(){
        viewModelScope.launch {
            _announcementState.value = AnnouncementState.LOADING
            refreshAnnouncementsUseCase()
            _announcementState.value = AnnouncementState.DEFAULT
        }
    }

    fun updateDisplayedAnnouncement(announcement: Announcement?){
        displayedAnnouncement = announcement
    }

    fun createAnnouncement(announcement: Announcement) {
        _announcementState.value = AnnouncementState.LOADING
        viewModelScope.launch {
            createAnnouncementUseCase(announcement)
                .onSuccess { _announcementState.value = AnnouncementState.ANNOUNCEMENT_CREATED }
                .onFailure { _announcementState.value = AnnouncementState.ERROR_ANNOUNCEMENT_CREATION }
        }
    }

    fun deleteAnnouncement(announcement: Announcement) {
        _announcementState.value = AnnouncementState.LOADING
        viewModelScope.launch {
            deleteAnnouncementUseCase(announcement)
            _announcementState.value = AnnouncementState.DEFAULT
        }
    }

    fun updateAnnouncement(announcement: Announcement) {
        _announcementState.value = AnnouncementState.LOADING
        viewModelScope.launch {
            updateAnnouncementUseCase(announcement)
            _announcementState.value = AnnouncementState.DEFAULT
        }
    }
}