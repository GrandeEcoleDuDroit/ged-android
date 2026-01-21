package com.upsaclay.gedoise.presentation.profile.accountinformation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.CustomError.CURRENT_USER_NOT_FOUND
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.gedoise.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class AccountInformationViewModel(
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase,
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AccountInformationUiState())
    val uiState: StateFlow<AccountInformationUiState> = _uiState

    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        userRepository.user
            .map { user ->
                _uiState.update { it.copy(user = user) }
            }
            .launchIn(viewModelScope)
    }

    fun updateProfilePicture() {
        executeRequest {
            uiState.value.profilePictureUri?.let { uri ->
                val user = uiState.value.user ?: throw CustomException(CURRENT_USER_NOT_FOUND, Exception())
                updateProfilePictureUseCase(user, uri.toString())
                _event.emit(SingleUiEvent.Success(R.string.profile_picture_updated))
            }
        }
    }

    fun deleteProfilePicture() {
        executeRequest {
            val user = uiState.value.user ?: throw CustomException(CURRENT_USER_NOT_FOUND, Exception())
            user.profilePictureUrl?.let {
                userRepository.deleteProfilePicture(user)
            }
            _event.emit(SingleUiEvent.Success(R.string.profile_picture_deleted))
        }
    }

    fun onScreenStateChange(screenState: AccountInformationScreenState) {
        _uiState.update {
            it.copy(screenState = screenState)
        }
    }

    fun resetScreenState() {
        _uiState.update {
            it.copy(
                screenState = AccountInformationScreenState.READ,
                profilePictureUri = null,
                loading = false
            )
        }
    }

    fun onProfilePictureUriChange(uri: Uri?) {
        _uiState.update {
            it.copy(profilePictureUri = uri)
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
                resetScreenState()
            }
        )
    }

    data class AccountInformationUiState(
        val user: User? = null,
        val profilePictureUri: Uri? = null,
        val loading: Boolean = false,
        val screenState: AccountInformationScreenState = AccountInformationScreenState.READ,
    )

    enum class AccountInformationScreenState {
        READ,
        EDIT
    }
}