package com.upsaclay.news.presentation.announcement.editannouncement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import com.upsaclay.news.presentation.announcement.AnnouncementPresentationUtils.MAX_CONTENT_LENGTH
import com.upsaclay.news.presentation.announcement.AnnouncementPresentationUtils.MAX_TITLE_LENGTH
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EditAnnouncementViewModel(
    private val announcement: Announcement,
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        EditAnnouncementUiState(
            title = announcement.title ?: "",
            content = announcement.content
        )
    )
    internal val uiState: StateFlow<EditAnnouncementUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    fun onTitleChange(title: String) {
        val truncatedTitle = title.take(MAX_TITLE_LENGTH)
        _uiState.update {
            it.copy(
                title = truncatedTitle,
                updateEnabled = validateUpdate(truncatedTitle, it.content)
            )
        }
    }

    fun onContentChange(content: String) {
        val truncatedContent = content.take(MAX_CONTENT_LENGTH)
        _uiState.update {
            it.copy(
                content = truncatedContent,
                updateEnabled = validateUpdate(it.title, truncatedContent)
            )
        }
    }

    fun updateAnnouncement() {
        val (title, content) = uiState.value
        if (!validateUpdate(title, content)) return

        val trimmedAnnouncement = announcement.copy(
            title = if (title.isBlank()) null else title.trim(),
            content = content
        )

        executeRequest {
            announcementRepository.updateAnnouncement(trimmedAnnouncement)
            _event.emit(SingleUiEvent.Success())
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

    private fun validateUpdate(title: String, content: String): Boolean =
        (validateTitle(title) || validateContent(content)) && validateMandatoryFields()

    private fun validateTitle(title: String): Boolean = title != announcement.title

    private fun validateContent(content: String): Boolean = content.trim() != announcement.content.trim()

    private fun validateMandatoryFields(): Boolean = uiState.value.content.isNotBlank()

    data class EditAnnouncementUiState(
        val title: String = "",
        val content: String = "",
        val loading: Boolean = false,
        val updateEnabled: Boolean = false
    )
}