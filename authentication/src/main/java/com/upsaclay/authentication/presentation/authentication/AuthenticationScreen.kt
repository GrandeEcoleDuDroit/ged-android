package com.upsaclay.authentication.presentation.authentication

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthenticationDestination(
    onRegistrationClick: () -> Unit,
    onLoginClick: () -> Unit,
    onForgottenPasswordClick: () -> Unit,
    viewModel: AuthenticationViewModel = koinViewModel()
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
                is SingleUiEvent.Success -> onLoginClick()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetValues()
        }
    }

    AuthenticationScreen(
        email = uiState.email,
        password = uiState.password,
        loading = uiState.loading,
        emailError = uiState.emailError,
        passwordError = uiState.passwordError,
        errorMessage = uiState.errorMessage,
        snackbarHostState = snackbarHostState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRegistrationClick = onRegistrationClick,
        onLoginClick = viewModel::login,
        onForgottenPasswordClick = onForgottenPasswordClick
    )
}

@Composable
private fun AuthenticationScreen(
    email: String,
    password: String,
    loading: Boolean,
    @StringRes emailError: Int? = null,
    @StringRes passwordError: Int? = null,
    errorMessage: Int? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegistrationClick: () -> Unit,
    onLoginClick: () -> Unit,
    onForgottenPasswordClick : () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .rootMediumPadding(innerPadding)
                .padding(top = dimensionResource(com.upsaclay.common.R.dimen.extra_large_padding))
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            HeaderSection()

            AuthenticationForm(
                email = email,
                password = password,
                loading = loading,
                emailError = emailError,
                passwordError = passwordError,
                errorMessage = errorMessage,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = {
                    focusManager.clearFocus()
                    onLoginClick()
                },
                onRegisterClick = {
                    focusManager.clearFocus()
                    onRegistrationClick()
                },
                onForgottenPasswordClick = {
                    focusManager.clearFocus()
                    onForgottenPasswordClick()
                }
            )
        }
    }
}

@Composable
private fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.mediumSpacing(),
    ) {
        Image(
            painter = painterResource(id = com.upsaclay.common.R.drawable.app_logo),
            contentDescription = stringResource(id = com.upsaclay.common.R.string.ged_logo_description),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier.width(160.dp)
        )

        Text(
            text = stringResource(id = com.upsaclay.common.R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
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
private fun AuthenticationScreenPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    GedoiseTheme {
       AuthenticationScreen(
           email = email,
           password = password,
           loading = false,
           onEmailChange = { email = it },
           onPasswordChange = { password = it },
           onRegistrationClick = {},
           onLoginClick = {},
           onForgottenPasswordClick = {}
       )
    }
}