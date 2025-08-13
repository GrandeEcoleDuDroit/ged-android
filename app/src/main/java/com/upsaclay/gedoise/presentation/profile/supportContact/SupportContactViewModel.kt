package com.upsaclay.gedoise.presentation.profile.supportContact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.gedoise.domain.usecase.SendMailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SupportContactViewModel(
    private val sendMailUseCase: SendMailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SupportContactUiState())

    val uiState : SharedFlow<SupportContactUiState> = _uiState

    fun sendMail() {
        viewModelScope.launch {
            sendMailUseCase(
                _uiState.value.subject,
                _uiState.value.message
            )
        }
    }

    fun onMessageChange(message: String?): Unit {
        _uiState.update { it.copy(message = message) }
    }

    fun onSubjectChange(subject: String?): Unit {
        _uiState.update { it.copy(subject = subject) }
    }

    data class SupportContactUiState(
        var subject : String? = null,
        var message : String? = null
    )
}