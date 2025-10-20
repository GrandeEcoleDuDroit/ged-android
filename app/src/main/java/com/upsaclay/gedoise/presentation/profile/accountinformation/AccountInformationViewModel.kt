package com.upsaclay.gedoise.presentation.profile.accountinformation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.DeleteProfilePictureUseCase
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.gedoise.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountInformationViewModel(
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase,
    private val deleteProfilePictureUseCase: DeleteProfilePictureUseCase,
    userRepository: UserRepository,
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {
    private val _uiState = MutableStateFlow(AccountInformationUiState())
    val uiState: StateFlow<AccountInformationUiState> = _uiState

    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        userRepository.user
            .map(::updateState)
            .launchIn(viewModelScope)
    }

    fun updateProfilePicture() {
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }

                val user = requireNotNull(_uiState.value.user)
                _uiState.value.profilePictureUri?.let { uri ->
                    updateState(loading = true)
                    updateProfilePictureUseCase(user, uri.toString())
                    cancelEdit()
                    _event.emit(SingleUiEvent.Success(R.string.profile_picture_updated))
                }
            } catch (e: Exception) {
                cancelEdit()
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    fun deleteProfilePicture() {
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }

                val user = requireNotNull(_uiState.value.user)
                updateState(loading = true)
                user.profilePictureUrl?.let {
                    deleteProfilePictureUseCase(user.id, it)
                }
                cancelEdit()
                _event.emit(SingleUiEvent.Success(R.string.profile_picture_deleted))
            } catch (e: Exception) {
                cancelEdit()
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            }
        }
    }

    fun onScreenStateChange(screenState: AccountInformationScreenState) {
        updateState(screenState = screenState)
    }

    fun cancelEdit() {
        updateState(
            screenState = AccountInformationScreenState.READ,
            profilePictureUri = null,
            loading = false
        )
    }

    fun onProfilePictureUriChange(uri: Uri?) {
        updateState(profilePictureUri = uri)

    }

    private fun updateState(
        user: User? = _uiState.value.user,
        profilePictureUri: Uri? = _uiState.value.profilePictureUri,
        loading: Boolean = _uiState.value.loading,
        screenState: AccountInformationScreenState = _uiState.value.screenState
    ) {
        _uiState.update {
            it.copy(
                user = user,
                profilePictureUri = profilePictureUri,
                loading = loading,
                screenState = screenState,
            )
        }
    }

    private fun mapErrorMessage(e: Exception): Int {
        return mapNetworkErrorMessage(e) {
            when (e) {
                is IllegalArgumentException -> com.upsaclay.common.R.string.current_user_not_found_error
                else -> com.upsaclay.common.R.string.unknown_error
            }
        }
    }

    data class AccountInformationUiState(
        val user: User? = null,
        val profilePictureUri: Uri? = null,
        val loading: Boolean = false,
        val screenState: AccountInformationScreenState = AccountInformationScreenState.READ,
    )
}

enum class AccountInformationScreenState {
    READ,
    EDIT
}