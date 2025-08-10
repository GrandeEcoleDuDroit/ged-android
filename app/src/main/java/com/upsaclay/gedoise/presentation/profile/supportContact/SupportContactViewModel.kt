package com.upsaclay.gedoise.presentation.profile.supportContact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SupportContactViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SupportContactUiState())

    val uiState : SharedFlow<SupportContactUiState> = _uiState

    fun sendMail() {
        TODO("Not yet implemented")
    }

    fun onMessageChange(message: String?): Unit {
        _uiState.update { it.copy(message = message) }
    }

    fun onObjetChange(objet: String?): Unit {
        _uiState.update { it.copy(objet = objet) }
    }

    data class SupportContactUiState(
        var objet : String? = null,
        var message : String? = null
    )
}