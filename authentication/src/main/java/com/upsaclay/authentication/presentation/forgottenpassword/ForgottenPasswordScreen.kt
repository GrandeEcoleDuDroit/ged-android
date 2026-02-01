package com.upsaclay.authentication.presentation.forgottenpassword

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgottenPasswordDestination(
    onBackClick: () -> Unit,
    viewModel : ForgottenPasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(it.messageId))
                is SingleUiEvent.Success -> showSnackBar(context.getString(it.messageId))
            }
        }
    }

    ForgottenPasswordScreen(
        email = uiState.email,
        loading = uiState.loading,
        emailError = uiState.emailError,
        errorMessage = uiState.errorMessage,
        snackbarHostState = snackbarHostState,
        onEmailChange = viewModel::onEmailChange,
        onSendPasswordResetClick = viewModel::resetPassword,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgottenPasswordScreen(
    email : String,
    loading: Boolean,
    @StringRes emailError: Int?,
    @StringRes errorMessage: Int?,
    snackbarHostState: SnackbarHostState,
    onEmailChange: (String) -> Unit,
    onSendPasswordResetClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.forgotten_password)
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(data)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
                .rootMediumPadding(innerPadding)
        ) {
            ForgottenPasswordForm(
                email = email,
                loading = loading,
                emailError = emailError,
                errorMessage = errorMessage,
                onEmailChange = onEmailChange,
                onSendPasswordResetClick = {
                    focusManager.clearFocus()
                    onSendPasswordResetClick()
                }
            )
        }
    }
}

/*
==================================================================
                                PREVIEW
====================================================================
*/

@PhonePreviews
@Composable
fun ForgottenPasswordScreenPreview() {
    GedoiseTheme {
        ForgottenPasswordScreen(
            email = "",
            loading = false,
            emailError = null,
            errorMessage = null,
            snackbarHostState = SnackbarHostState(),
            onEmailChange = {},
            onSendPasswordResetClick = {},
            onBackClick = {}
        )
    }
}