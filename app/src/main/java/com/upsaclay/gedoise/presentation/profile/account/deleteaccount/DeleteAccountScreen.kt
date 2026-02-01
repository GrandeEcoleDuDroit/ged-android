package com.upsaclay.gedoise.presentation.profile.account.deleteaccount

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.presentation.components.OutlinePasswordTextField
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.gedoise.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeleteAccountDestination(
    onBackClick: () -> Unit,
    viewModel: DeleteAccountViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DeleteAccountScreen(
        onBackClick = onBackClick,
        password = uiState.password,
        passwordError = uiState.passwordError,
        loading = uiState.loading,
        errorMessage = uiState.errorMessage,
        onPasswordChange = viewModel::onPasswordChange,
        onDeleteAccountClick = viewModel::deleteUserAccount
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteAccountScreen(
    onBackClick: () -> Unit,
    password: String,
    @StringRes passwordError: Int? = null,
    loading: Boolean = false,
    errorMessage: Int? = null,
    onPasswordChange: (String) -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        topBar = {
            BackTopBar(
                onBackClick = {
                    focusManager.clearFocus()
                    onBackClick()
                },
                title = stringResource(R.string.delete_account)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.rootMediumPadding(innerPadding),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            Text(text = stringResource(R.string.delete_account_warning))

            OutlinePasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                text = password,
                onValueChange = onPasswordChange,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                errorMessage = passwordError
            )

            errorMessage?.let {
                Text(
                    text = stringResource(it),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDeleteAccountClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.delete_account)
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

@PhonePreviews
@Composable
private fun DeleteAccountScreenPreview() {
    GedoiseTheme {
        DeleteAccountScreen(
            onBackClick = {},
            password = "",
            loading = false,
            onPasswordChange = {},
            onDeleteAccountClick = {}
        )
    }
}