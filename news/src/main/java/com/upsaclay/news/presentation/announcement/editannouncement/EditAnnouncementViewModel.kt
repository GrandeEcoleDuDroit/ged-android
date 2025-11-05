package com.upsaclay.news.presentation.announcement.editannouncement

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.Companion.CONTENT_MAX_LENGTH
import com.upsaclay.news.domain.entity.Announcement.Companion.TITLE_MAX_LENGTH
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAnnouncementViewModel(
    private val announcement: Announcement,
    private val announcementRepository: AnnouncementRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        EditAnnouncementUiState(
            title = announcement.title ?: "",
            content = TextFieldValue(
                text = announcement.content,
                selection = TextRange(announcement.content.length)
            )
        )
    )
    internal val uiState: StateFlow<EditAnnouncementUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    fun onTitleChange(title: String) {
        val titleTruncated = title.take(TITLE_MAX_LENGTH)
        _uiState.update {
            it.copy(
                title = titleTruncated,
                updateEnabled = validateUpdate(titleTruncated, it.content.text)
            )
        }
    }

    fun onContentChange(content: TextFieldValue) {
        val contentTruncated = content.text.take(CONTENT_MAX_LENGTH)
        _uiState.update {
            it.copy(
                content = content.copy(text = contentTruncated),
                updateEnabled = validateUpdate(it.title, contentTruncated)
            )
        }
    }

    fun updateAnnouncement() {
        if (!validateUpdate(uiState.value.title, uiState.value.content.text)) {
            return
        }

        val trimmedAnnouncement = announcement.copy(
            title = uiState.value.title.trim(),
            content = uiState.value.content.text.trim()
        )

        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }
                _uiState.update {
                    it.copy(loading = true)
                }
                announcementRepository.updateAnnouncement(trimmedAnnouncement)
                _event.emit(SingleUiEvent.Success())
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun validateUpdate(title: String, content: String): Boolean =
        validateTitle(title) || validateContent(content)

    private fun validateTitle(title: String): Boolean =
        title != announcement.title && uiState.value.content.text.isNotBlank()

    private fun validateContent(content: String): Boolean =
        content.trim() != announcement.content.trim() && content.isNotBlank()

    data class EditAnnouncementUiState(
        val title: String = "",
        val content: TextFieldValue = TextFieldValue(),
        val loading: Boolean = false,
        val updateEnabled: Boolean = false
    )
}