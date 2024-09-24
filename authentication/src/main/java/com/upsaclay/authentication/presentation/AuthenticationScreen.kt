package com.upsaclay.authentication.presentation

import android.os.Build
import android.view.ViewTreeObserver
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.model.AuthenticationState
import com.upsaclay.authentication.presentation.components.LargeButton
import com.upsaclay.authentication.presentation.components.LoadingLargeButton
import com.upsaclay.authentication.presentation.components.OutlinedEmailInput
import com.upsaclay.authentication.presentation.components.OutlinedPasswordInput
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.presentation.components.ErrorText
import com.upsaclay.common.presentation.components.OverlayLoadingScreen
import com.upsaclay.common.presentation.theme.GedoiseColor.Primary
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel
import com.upsaclay.common.R as CoreResource

@Composable
fun AuthenticationScreen(
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel = koinViewModel()
) {
    val authenticationState by authenticationViewModel.authenticationState.collectAsState()
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var isKeyboardVisible by remember { mutableStateOf(false) }
    val view = LocalView.current
    val context = LocalContext.current
    val email = authenticationViewModel.email
    val password = authenticationViewModel.password

    if (authenticationState == AuthenticationState.AUTHENTICATED) {
        navController.navigate(Screen.NEWS.route)
    }

    isError = authenticationState == AuthenticationState.AUTHENTICATION_ERROR ||
            authenticationState == AuthenticationState.INPUT_ERROR

    errorMessage = when (authenticationState) {
        AuthenticationState.AUTHENTICATION_ERROR ->
            stringResource(id = R.string.error_connection)

        AuthenticationState.INPUT_ERROR ->
            stringResource(id = CoreResource.string.error_empty_fields)

        else -> ""
    }

    DisposableEffect(context) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val insets = view.rootWindowInsets
            val keyboardHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
            } else {
                insets?.systemWindowInsetBottom ?: 0
            }
            isKeyboardVisible = keyboardHeight > 0
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }


    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.aligned { size, space ->
                size + (30 * space / 100)
            },
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(MaterialTheme.spacing.medium)
        ) {
            TitleSection()

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            BottomSection(
                email = email,
                onEmailChange = { authenticationViewModel.updateEmail(it) },
                password = password,
                onPasswordChange = { authenticationViewModel.updatePassword(it) },
                errorMessage = errorMessage,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                onConnectionClick = {
                    keyboardController?.hide()
                    authenticationViewModel.login()
                },
                onRegistrationClick = { navController.navigate(Screen.FIRST_REGISTRATION_SCREEN.route) },
                isError = isError,
                isEnable = authenticationState != AuthenticationState.LOADING
            )
        }

        if (authenticationState == AuthenticationState.LOADING) {
            OverlayLoadingScreen()
        }
    }
}

@Composable
private fun TitleSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = CoreResource.drawable.ged_logo),
            contentDescription = stringResource(id = CoreResource.string.ged_logo_description),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier.size(140.dp)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        Text(
            text = stringResource(id = R.string.welcome_text),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = stringResource(id = R.string.presentation_text),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Primary
        )
    }
}

@Composable
private fun BottomSection(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    isError: Boolean,
    isEnable: Boolean = true,
    errorMessage: String,
    onConnectionClick: () -> Unit,
    onRegistrationClick: () -> Unit,
    isLoading: Boolean = false
) {
    Column {
        InputsSection(
            email = email,
            onEmailChange = onEmailChange,
            password = password,
            onPasswordChange = onPasswordChange,
            keyboardActions = keyboardActions,
            errorMessage = errorMessage,
            isError = isError,
            isEnable = isEnable
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        if(isLoading) {
            LoadingLargeButton(modifier = Modifier.fillMaxWidth())
        } else {
            LargeButton(
                text = stringResource(id = R.string.login),
                onClick = onConnectionClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = stringResource(id = R.string.first_arrival))

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

            TextButton(
                contentPadding = PaddingValues(MaterialTheme.spacing.default),
                modifier = Modifier.height(MaterialTheme.spacing.large),
                onClick = onRegistrationClick,
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up),
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun InputsSection(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    errorMessage: String,
    isError: Boolean,
    isEnable: Boolean = true
) {
    Column {
        OutlinedEmailInput(
            text = email,
            onValueChange = onEmailChange,
            keyboardActions = keyboardActions,
            isError = isError,
            isEnable = isEnable
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        OutlinedPasswordInput(
            text = password,
            onValueChange = onPasswordChange,
            keyboardActions = keyboardActions,
            isError = isError,
            isEnable = isEnable
        )
        if (isError) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            ErrorText(text = errorMessage)
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(widthDp = 360, heightDp = 740, showBackground = true)
@Composable
private fun AuthenticationScreenPreview() {
    var isLoading by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    GedoiseTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.aligned { size, space ->
                size + (20 * space / 100)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        ) {
            TitleSection()

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            BottomSection(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                errorMessage = "Veuillez remplir tous les champs",
                onConnectionClick = { isLoading = !isLoading },
                onRegistrationClick = { },
                keyboardActions = KeyboardActions.Default,
                isError = false,
                isEnable = !isLoading,
                isLoading = isLoading
            )
        }
    }
}
