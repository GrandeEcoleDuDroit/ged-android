package com.upsaclay.authentication.ui.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.model.RegistrationState
import com.upsaclay.authentication.ui.components.OutlinedEmailInput
import com.upsaclay.authentication.ui.components.OutlinedPasswordInput
import com.upsaclay.authentication.ui.components.RegistrationTopBar
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.ui.components.ErrorText
import com.upsaclay.common.ui.components.OverlayLoadingScreen
import com.upsaclay.common.ui.components.PrimaryLargeButton
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing

import org.koin.androidx.compose.koinViewModel

@Composable
fun SecondRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel = koinViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val registrationState by registrationViewModel.registrationState.collectAsState()
    var errorMessage by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        registrationViewModel.resetProfilePictureUri()
    }

    LaunchedEffect(registrationState) {
        if (registrationState == RegistrationState.RECOGNIZED_ACCOUNT) {
            navController.navigate(Screen.THIRD_REGISTRATION_SCREEN.route)
        }
    }

    isError = registrationState == RegistrationState.UNRECOGNIZED_ACCOUNT ||
            registrationState == RegistrationState.INPUT_ERROR

    errorMessage = when (registrationState) {
        RegistrationState.UNRECOGNIZED_ACCOUNT ->
            stringResource(id = R.string.unrecognized_account)

        RegistrationState.INPUT_ERROR ->
            stringResource(id = com.upsaclay.common.R.string.error_empty_fields)

        else -> ""
    }

    RegistrationTopBar(
        navController = navController,
        currentStep = 2,
        maxStep = MAX_REGISTRATION_STEP
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Text(
                text = stringResource(id = R.string.enter_school_credentials),
                style = MaterialTheme.typography.titleMedium,
            )
            OutlinedEmailInput(
                text = registrationViewModel.email,
                onValueChange = { mailText ->
                    registrationViewModel.updateEmail(mailText)
                },
                isError = isError
            )

            Spacer(Modifier.height(MaterialTheme.spacing.small))

            OutlinedPasswordInput(
                text = registrationViewModel.password,
                isError = isError,
                onValueChange = { passwordText ->
                    registrationViewModel.updatePassword(passwordText)
                }
            )

            if (isError) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                ErrorText(text = errorMessage)
            }
        }

        PrimaryLargeButton(
            text = stringResource(id = com.upsaclay.common.R.string.next),
            onClick = {
                keyboardController?.hide()
                registrationViewModel.verifyAccount(
                    registrationViewModel.email,
                    registrationViewModel.password
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }

    if (registrationState == RegistrationState.LOADING) {
        OverlayLoadingScreen()
    }
}

@Preview
@Composable
private fun SecondRegistrationScreenPreview() {
    val mail = "pierre.dupont@universite-paris-saclay.fr"
    val password = "password"
    val isLoading = false

    GedoiseTheme {
        RegistrationTopBar(
            navController = rememberNavController(),
            currentStep = 2,
            maxStep = MAX_REGISTRATION_STEP
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.enter_school_credentials),
                    style = MaterialTheme.typography.titleMedium,
                )

                OutlinedEmailInput(
                    text = mail,
                    onValueChange = {},
                )
                Spacer(Modifier.height(MaterialTheme.spacing.small))

                OutlinedPasswordInput(
                    text = password,
                    onValueChange = {},
                )
            }

            PrimaryLargeButton(
                text = stringResource(id = com.upsaclay.common.R.string.next),
                onClick = { },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }

    if (isLoading) {
        OverlayLoadingScreen()
    }
}
