package com.upsaclay.authentication.presentation.registration

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.model.RegistrationState
import com.upsaclay.authentication.domain.usecase.IsAccountExistUseCase
import com.upsaclay.authentication.domain.usecase.RegistrationUseCase
import com.upsaclay.common.domain.model.User
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal const val MAX_REGISTRATION_STEP = 3

class RegistrationViewModel(
    private val isAccountExistUseCase: IsAccountExistUseCase,
    private val registrationUseCase: RegistrationUseCase
) : ViewModel() {
    private val _registrationState = MutableStateFlow(RegistrationState.NOT_REGISTERED)
    val registrationState: StateFlow<RegistrationState> = _registrationState
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    val schoolLevels = persistentListOf("GED 1", "GED 2", "GED 3", "GED 4")
    var schoolLevel by mutableStateOf(schoolLevels[0])
        private set
    var profilePictureUri: Uri? by mutableStateOf(null)
        private set
    var fullName by mutableStateOf("")
        private set

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateSchoolLevel(schoolLevel: String) {
        this.schoolLevel = schoolLevel
    }

    fun updateProfilePictureUri(uri: Uri?) {
        uri?.let { profilePictureUri = it }
    }

    fun resetEmail() {
        email = ""
    }

    fun resetPassword() {
        password = ""
    }

    fun resetProfilePictureUri() {
        profilePictureUri = null
    }

    fun verifyAccount(email: String, password: String) {
        _registrationState.value = RegistrationState.LOADING

        if (email.isBlank() || password.isBlank()) {
            _registrationState.value = RegistrationState.INPUT_ERROR
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

        val user = com.upsaclay.common.domain.model.User(
            firstName = fullName.split(" ")[0],
            lastName = fullName.split(" ")[1],
            email = email,
            schoolLevel = schoolLevel
        )

        viewModelScope.launch {
            registrationUseCase(user, profilePictureUri)
                .onSuccess { _registrationState.value = RegistrationState.REGISTERED }
                .onFailure { _registrationState.value = RegistrationState.REGISTRATION_ERROR }
        }
    }
}