package com.upsaclay.authentication.presentation.forgottenpassword

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R as authR
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
    viewModel: ForgottenPasswordViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
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
        onBackClick = onBackClick,
        onEmailChange = viewModel::onEmailChange,
        onClick = { suspend { viewModel.sendResetEmail(it) } },
        email = uiState.email,
        onEmailError = uiState.emailError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgottenPasswordScreen(
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onClick: () -> Unit,
    email : String,
    onEmailError : Int?
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(authR.string.forgotten_password)
            )
        },

    ){
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .rootMediumPadding(innerPadding)
                .pointerInput(Unit){
                    detectTapGestures(
                        onTap = {focusManager.clearFocus()}
                    )
                },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.aligned {
                size, space -> size + (10*space/100)
            }

        ) {
            ForgottenPasswordForm(
                onEmailChange = onEmailChange,
                onClick = onClick,
                email = email,
                onEmailError = onEmailError

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
            onBackClick = {},
            onEmailChange = {},
            onClick = {},
            email = "",
            onEmailError = null
        )
    }
}