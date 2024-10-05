package com.upsaclay.news.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateAnnouncementViewModel(private val createAnnouncementUseCase: com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase) :
    ViewModel() {
    private val _announcementState =
        MutableStateFlow(com.upsaclay.news.domain.model.AnnouncementState.DEFAULT)
    val announcementState: Flow<com.upsaclay.news.domain.model.AnnouncementState> =
        _announcementState
    var title: String by mutableStateOf("")
        private set
    var content: String by mutableStateOf("")
        private set

    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateContent(content: String) {
        this.content = content
    }

    fun createAnnouncement(announcement: com.upsaclay.news.domain.model.Announcement) {
        _announcementState.value = com.upsaclay.news.domain.model.AnnouncementState.LOADING
        viewModelScope.launch {
            delay(300)
            createAnnouncementUseCase(announcement)
                .onSuccess {
                    _announcementState.value =
                        com.upsaclay.news.domain.model.AnnouncementState.ANNOUNCEMENT_CREATED
                }
                .onFailure {
                    _announcementState.value =
                        com.upsaclay.news.domain.model.AnnouncementState.ANNOUNCEMENT_CREATION_ERROR
                }
        }
    }
}