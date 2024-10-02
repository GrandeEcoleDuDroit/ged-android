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

class EditAnnouncementViewModel(
    val editedAnnouncement: com.upsaclay.news.domain.model.Announcement,
    private val updateAnnouncementUseCase: com.upsaclay.news.domain.usecase.UpdateAnnouncementUseCase
) : ViewModel() {
    private val _announcementState =
        MutableStateFlow(com.upsaclay.news.domain.model.AnnouncementState.DEFAULT)
    val announcementState: Flow<com.upsaclay.news.domain.model.AnnouncementState> =
        _announcementState

    private val _isAnnouncementModified = MutableStateFlow(false)
    val isAnnouncementModified: Flow<Boolean> = _isAnnouncementModified

    var title by mutableStateOf(editedAnnouncement.title ?: "")
        private set
    var content: String by mutableStateOf(editedAnnouncement.content)
        private set

    fun updateTitle(title: String) {
        this.title = title
        verifyIsAnnouncementModified()
    }

    fun updateContent(content: String) {
        this.content = content
        verifyIsAnnouncementModified()
    }

    fun updateAnnouncement(announcement: com.upsaclay.news.domain.model.Announcement) {
        _announcementState.value = com.upsaclay.news.domain.model.AnnouncementState.LOADING
        viewModelScope.launch {
            delay(300)
            updateAnnouncementUseCase(announcement)
                .onSuccess {
                    _announcementState.value =
                        com.upsaclay.news.domain.model.AnnouncementState.ANNOUNCEMENT_UPDATED
                }
                .onFailure {
                    _announcementState.value =
                        com.upsaclay.news.domain.model.AnnouncementState.ANNOUNCEMENT_UPDATE_ERROR
                }

        }
    }

    private fun verifyIsAnnouncementModified() {
        val isDifferentTitle = title.trim() != editedAnnouncement.title
        val isDifferentContent = content.trim() != editedAnnouncement.content
        _isAnnouncementModified.value = isDifferentTitle || isDifferentContent
    }
}