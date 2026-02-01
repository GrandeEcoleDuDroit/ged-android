package com.upsaclay.authentication.presentation.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.OutlinePasswordTextField
import com.upsaclay.common.extension.extraSmallSpacing
import com.upsaclay.common.presentation.components.LoadingButton
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun AuthenticationForm(
    email: String,
    password: String,
    loading: Boolean,
    emailError: Int?,
    passwordError: Int?,
    errorMessage: Int?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgottenPasswordClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
    ) {
        CredentialsInputs(
            email = email,
            password = password,
            loading = loading,
            emailError = emailError,
            passwordError = passwordError,
            errorMessage = errorMessage,
            passwordFocusRequester = passwordFocusRequester,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onForgottenPasswordClick = onForgottenPasswordClick
        )

        LoadingButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(id = R.string.authentication_screen_login_button_tag)),
            text = stringResource(id = R.string.login),
            loading = loading,
            onClick = {
                focusManager.clearFocus()
                onLoginClick()
            }
        )

        RegistrationText(
            loading = loading,
            onRegistrationClick = {
                focusManager.clearFocus()
                onRegisterClick()
            }
        )
    }
}


@Composable
private fun CredentialsInputs(
    email: String,
    password: String,
    loading: Boolean,
    emailError: Int?,
    passwordError: Int?,
    errorMessage: Int?,
    passwordFocusRequester: FocusRequester,
    keyboardActions: KeyboardActions,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgottenPasswordClick : () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
    ) {
        SimpleOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            label = stringResource(com.upsaclay.common.R.string.email),
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            errorMessage = emailError?.let { stringResource(it) },
            enabled = !loading
        )

        OutlinePasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            text = password,
            onValueChange = onPasswordChange,
            keyboardActions = keyboardActions,
            errorMessage = passwordError,
            enabled = !loading
        )

        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .clickable(onClick = onForgottenPasswordClick)
                .testTag(stringResource(R.string.forgotten_password_button_tag)),
            text = stringResource(R.string.forgotten_password_button_text),
            style = MaterialTheme.typography.labelLarge
        )

        errorMessage?.let {
            LaunchedEffect(it) {
                passwordFocusRequester.requestFocus()
            }

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun RegistrationText(
    loading: Boolean,
    onRegistrationClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.extraSmallSpacing()
    ) {
        Text(
            text = stringResource(id = R.string.not_register_yet),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = AnnotatedString(stringResource(id = R.string.sign_up)),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .clickable(enabled = !loading, onClick = onRegistrationClick)
                .testTag(stringResource(id = R.string.authentication_screen_registration_button_tag))
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun AuthenticationFormPreview() {
    GedoiseTheme {
        Surface {
            AuthenticationForm(
                email = "",
                password = "",
                loading = false,
                emailError = null,
                passwordError = null,
                errorMessage = null,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onRegisterClick = {},
                onForgottenPasswordClick = {}
            )
        }
    }
}