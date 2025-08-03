package com.upsaclay.forum.presentation

import androidx.lifecycle.ViewModel
import com.upsaclay.forum.domain.entity.Mission
import kotlinx.coroutines.flow.MutableStateFlow

class ForumViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MissionUiState())
    internal val uiState = _uiState

    internal data class MissionUiState(
        val missions: List<Mission> = emptyList(),
    )
}