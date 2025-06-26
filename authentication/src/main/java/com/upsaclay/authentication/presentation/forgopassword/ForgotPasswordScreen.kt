package com.upsaclay.authentication.presentation.forgopassword

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.authentication.presentation.authentication.AuthenticationViewModel
import com.upsaclay.common.domain.entity.FcmMessage
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.authentication.R as authenticationR
import com.upsaclay.common.R as commonR
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.OutlineTextField
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.LinearProgressBar
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.mediumPadding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordDestination(
    onBackClick: () -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel()
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

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetValues()
        }
    }

    ForgotPasswordScreen(
        email = uiState.email,
        onBackClick = onBackClick,
        loading = uiState.loading,
        onValueChange = viewModel::onEmailChange,
        onButtonClick = viewModel::sendMail
    )
}
@Composable
fun ForgotPasswordScreen(
    email : String,
    @StringRes errorMessage: Int? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick : () -> Unit,
    loading : Boolean = false,
    onValueChange : (String) -> Unit,
    onButtonClick : () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        Surface {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                modifier = Modifier
                    .fillMaxSize()
                    .mediumPadding(innerPadding)
                    .verticalScroll(scrollState)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                focusManager.clearFocus()
                            }
                        )
                    }
            ) {
                BackTopBar(onBackClick = onBackClick,
                    title = stringResource(authenticationR.string.forgot_password)
                )

                if (loading) {
                    LinearProgressBar(modifier = Modifier.fillMaxWidth())
                }

                Text(
                    text = stringResource(id = authenticationR.string.enter_email),
                    fontWeight = FontWeight.SemiBold,

                    )

                OutlineTextField(modifier = Modifier.fillMaxWidth(0.90f),
                    value = email,
                    onValueChange = onValueChange,
                    label = stringResource(id = commonR.string.email),
                    errorMessage = errorMessage

                )

                PrimaryButton(
                    text = stringResource(id = commonR.string.send),
                    onClick = onButtonClick,
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .height(MaterialTheme.spacing.extraLarge + 5.dp)

                )
            }
        }

    }
}



/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Composable
@Preview
fun ForgotPasswordPreview() {
    val email by remember { mutableStateOf("") }

    GedoiseTheme {
        ForgotPasswordScreen(
            email = email,
            onBackClick = { },
            onValueChange = { },
            onButtonClick = { }
        )
    }
}