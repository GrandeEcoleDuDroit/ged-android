package com.upsaclay.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.usecase.GetAllAnnouncementsUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NewsViewModel(
    getAllAnnouncementUseCase: GetAllAnnouncementsUseCase,
    private val refreshAnnouncementsUseCase: RefreshAnnouncementsUseCase,
): ViewModel() {
    val announcements: Flow<List<Announcement>> = getAllAnnouncementUseCase()
    lateinit var displayedAnnouncement: Announcement
        private set

    fun refreshAnnouncements(){
        viewModelScope.launch { refreshAnnouncementsUseCase() }
    }

    fun updateDisplayedAnnouncement(announcement: Announcement){
        displayedAnnouncement = announcement
    }
}