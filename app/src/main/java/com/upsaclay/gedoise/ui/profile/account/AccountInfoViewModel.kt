package com.upsaclay.gedoise.ui.profile.account

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.usecase.GetCurrentUserFlowUseCase
import com.upsaclay.common.domain.usecase.IsUserHasDefaultProfilePictureUseCase
import com.upsaclay.common.domain.usecase.ResetUserProfilePictureUseCase
import com.upsaclay.common.domain.usecase.UpdateUserProfilePictureUseCase
import com.upsaclay.gedoise.data.profile.AccountInfoScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AccountInfoViewModel(
    private val updateUserProfilePictureUseCase: UpdateUserProfilePictureUseCase,
    private val resetProfilePictureUseCase: ResetUserProfilePictureUseCase,
    getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase,
    isUserHasDefaultProfilePictureUseCase: IsUserHasDefaultProfilePictureUseCase
): ViewModel() {
    private val _accountInfoScreenState = MutableStateFlow(AccountInfoScreenState.READ)
    val accountScreenState = _accountInfoScreenState.asStateFlow()
    val isUserHasDefaultProfilePicture: Flow<Boolean> = isUserHasDefaultProfilePictureUseCase()
    val currentUser = getCurrentUserFlowUseCase()
    var profilePictureUri by mutableStateOf<Uri?>(null)
        private set

    fun updateProfilePictureUri(uri: Uri) {
        profilePictureUri = uri
    }

    fun updateAccountScreenState(screenState: AccountInfoScreenState) {
        _accountInfoScreenState.value = screenState
    }

    fun resetProfilePictureUri() {
        profilePictureUri = null
    }

    fun updateUserProfilePicture() {
        _accountInfoScreenState.value = AccountInfoScreenState.LOADING
        profilePictureUri?.let {
            viewModelScope.launch {
                updateUserProfilePictureUseCase(currentUser.first().id, it)
                    .onSuccess {
                        _accountInfoScreenState.value = AccountInfoScreenState.PROFILE_PICTURE_UPDATED
                    }
                    .onFailure {
                        _accountInfoScreenState.value = AccountInfoScreenState.ERROR_UPDATING_PROFILE_PICTURE
                    }
            }
            resetProfilePictureUri()
        }
    }

    fun deleteUserProfilePicture() {
        _accountInfoScreenState.value = AccountInfoScreenState.LOADING
        viewModelScope.launch {
            resetProfilePictureUseCase(currentUser.first().id)
                .onSuccess { _accountInfoScreenState.value = AccountInfoScreenState.PROFILE_PICTURE_UPDATED }
                .onFailure { _accountInfoScreenState.value = AccountInfoScreenState.ERROR_UPDATING_PROFILE_PICTURE }
        }
        resetProfilePictureUri()
    }
}