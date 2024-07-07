package com.upsaclay.news.ui

import androidx.lifecycle.ViewModel
import com.upsaclay.news.data.Post
import com.upsaclay.news.data.model.Announcement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsViewModel: ViewModel() {
    private val _announcements: MutableStateFlow<List<Announcement>> = MutableStateFlow(emptyList())
    private val _posts: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private fun fetchAnnouncements(){

    }
}