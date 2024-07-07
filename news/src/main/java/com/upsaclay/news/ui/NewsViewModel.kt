package com.upsaclay.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.news.data.model.Announcement
import com.upsaclay.news.domain.GetAllAnnouncementUseCase
import com.upsaclay.news.domain.UpdateAnnouncementsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NewsViewModel(
    getAllAnnouncementUseCase: GetAllAnnouncementUseCase,
    private val updateAnnouncementsUseCase: UpdateAnnouncementsUseCase
): ViewModel() {
    val announcements: Flow<List<Announcement>> = getAllAnnouncementUseCase()

    fun updateAnnouncements(){
        viewModelScope.launch {
            updateAnnouncementsUseCase()
        }
    }
}