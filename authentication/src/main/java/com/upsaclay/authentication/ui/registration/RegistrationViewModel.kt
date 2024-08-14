package com.upsaclay.authentication.ui.registration

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.model.RegistrationState
import com.upsaclay.authentication.domain.usecase.IsAccountExistUseCase
import com.upsaclay.authentication.domain.usecase.RegistrationUseCase
import com.upsaclay.common.R
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetDrawableUriUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal const val MAX_REGISTRATION_STEP = 3

class RegistrationViewModel(
    getDrawableUriUseCase: GetDrawableUriUseCase,
    private val isAccountExistUseCase: IsAccountExistUseCase,
    private val registrationUseCase: RegistrationUseCase,
) : ViewModel() {

    private val _registrationState = MutableStateFlow(RegistrationState.NOT_REGISTERED)
    val registrationState: StateFlow<RegistrationState> = _registrationState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    val schoolLevels = persistentListOf("GED 1", "GED 2", "GED 3", "GED 4")
    var currentSchoolLevel by mutableStateOf(schoolLevels[0])
        private set

    val defaultPictureUri = getDrawableUriUseCase(R.drawable.default_profile_picture)!!
    var profilePictureUri: Uri by mutableStateOf(defaultPictureUri)
        private set

    var fullName by mutableStateOf("")
        private set

    fun updateEmail(value: String) { email = value }

    fun updatePassword(value: String) { password = value }

    fun updateSchoolLevel(value: String) { currentSchoolLevel = value }

    fun updateProfilePictureUri(uri: Uri?) { uri?.let { profilePictureUri = it } }

    fun resetEmail() { email = "" }

    fun resetPassword() { password = "" }

    fun resetProfilePictureUri() { profilePictureUri = defaultPictureUri }

    fun verifyAccount(email: String, password: String) {
        _registrationState.value = RegistrationState.LOADING

        if (email.isBlank() || password.isBlank()) {
            _registrationState.value = RegistrationState.ERROR_INPUT
            return
        }

        viewModelScope.launch {
            _registrationState.value = if (isAccountExistUseCase(email, password)) {
                val splitName = email.split(".")
                val splitMail = splitName[1].split("@")

                val firstname = splitName[0].replaceFirstChar { it.uppercase() }
                val lastname = splitMail[0].replaceFirstChar { it.uppercase() }

                fullName = String.format("%s %s", firstname, lastname)
                RegistrationState.RECOGNIZED_ACCOUNT
            } else {
                RegistrationState.UNRECOGNIZED_ACCOUNT
            }
        }
    }

    fun register() {
        _registrationState.value = RegistrationState.LOADING

        val user = User(
            firstName = fullName.split(" ")[0],
            lastName = fullName.split(" ")[1],
            email = email,
            schoolLevel = currentSchoolLevel
        )

        viewModelScope.launch {
            registrationUseCase(user, profilePictureUri)
                .onSuccess { _registrationState.value = RegistrationState.REGISTERED }
                .onFailure { _registrationState.value = RegistrationState.ERROR_REGISTRATION }
        }
    }
}