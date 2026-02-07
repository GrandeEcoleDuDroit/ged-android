package com.upsaclay.news.presentation.announcement.createannouncement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.entity.Announcement.Companion.MAX_CONTENT_LENGTH
import com.upsaclay.news.domain.entity.Announcement.Companion.MAX_TITLE_LENGTH
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateAnnouncementViewModel(
    userRepository: UserRepository,
    private val createAnnouncementUseCase: CreateAnnouncementUseCase,
    private val generateIdUseCase: GenerateIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateAnnouncementUiState())
    internal val uiState: StateFlow<CreateAnnouncementUiState> = _uiState
    private val user: User? = userRepository.currentUser

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(
                title = title.take(MAX_TITLE_LENGTH),
                createEnabled = validateCreate(uiState.value.content)
            )
        }
    }

    fun onContentChange(content: String) {
        val contentTruncated = content.take(MAX_CONTENT_LENGTH)
        _uiState.update {
            it.copy(
                content = contentTruncated,
                createEnabled = validateCreate(contentTruncated)
            )
        }
    }

    fun createAnnouncement() {
        if (user == null) return
        val (title, content) = uiState.value
        val announcement = Announcement(
            id = generateIdUseCase.execute(),
            title = if (title.isBlank()) null else title.trim(),
            content = content.trim(),
            date = LocalDateTime.now(ZoneOffset.UTC),
            author = user,
            state = AnnouncementState.DRAFT
        )
        viewModelScope.launch {
            createAnnouncementUseCase.execute(announcement)
        }
    }

    private fun validateCreate(content: String): Boolean = content.isNotBlank()

    internal data class CreateAnnouncementUiState(
        val title: String = "",
        val content: String = "",
        val createEnabled: Boolean = false
    )
}