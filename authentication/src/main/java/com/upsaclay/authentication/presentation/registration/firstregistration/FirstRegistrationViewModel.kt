package com.upsaclay.authentication.presentation.registration.firstregistration

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.upsaclay.authentication.R
import com.upsaclay.common.domain.UserUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

private const val MAX_NAME_LENGTH: Int = 50

class FirstRegistrationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FirstRegistrationUiState())
    internal val uiState: StateFlow<FirstRegistrationUiState> = _uiState

    fun onFirstNameChange(firstName: String) {
        val truncatedFirstName = firstName.take(MAX_NAME_LENGTH)
        if (validateName(truncatedFirstName)) {
            _uiState.update {
                it.copy(firstName = truncatedFirstName)
            }
        }
    }

    fun onLastNameChange(lastName: String) {
        val truncatedLastName = lastName.take(MAX_NAME_LENGTH)
        if (validateName(truncatedLastName)) {
            _uiState.update {
                it.copy(lastName = truncatedLastName)
            }
        }
    }

    fun validateInputs(): Boolean {
        val (firstName, lastName) = uiState.value

        _uiState.update {
            it.copy(
                firstName = UserUtils.Name.formatName(firstName.trim()),
                lastName = UserUtils.Name.formatName(lastName.trim()),
                firstNameError = R.string.mandatory_field.takeIf { firstName.isBlank() },
                lastNameError = R.string.mandatory_field.takeIf { lastName.isBlank() }
            )
        }

        return with(_uiState.value) {
            firstNameError == null && lastNameError == null
        }
    }

    private fun validateName(name: String): Boolean =
        name.matches(Regex("^[\\p{L}'\\s-]+$")) || name.isBlank()

    internal data class FirstRegistrationUiState(
        val firstName: String = "",
        val lastName: String = "",
        @StringRes val firstNameError: Int? = null,
        @StringRes val lastNameError: Int? = null
    )
}