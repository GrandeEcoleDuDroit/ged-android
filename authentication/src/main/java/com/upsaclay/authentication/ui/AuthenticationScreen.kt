package com.upsaclay.authentication.ui

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
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
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
import com.upsaclay.authentication.data.model.AuthenticationState
import com.upsaclay.authentication.ui.components.OutlinedEmailInput
import com.upsaclay.authentication.ui.components.OutlinedPasswordInput
import com.upsaclay.core.data.model.Screen
import com.upsaclay.core.ui.components.LoadingScreen
import com.upsaclay.core.ui.components.PrimaryLargeButton
import com.upsaclay.core.ui.theme.GedoiseColor.BackgroundVariant
import com.upsaclay.core.ui.theme.GedoiseColor.Primary
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.ui.theme.spacing
import org.koin.androidx.compose.koinViewModel
import com.upsaclay.core.R as CoreResource

@Composable
fun AuthenticationScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
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

//    if(authenticationState == AuthenticationState.AUTHENTICATED){
//        navController.navigate(Screen.HOME.route)
//    }

    isError = authenticationState == AuthenticationState.ERROR_AUTHENTICATION ||
            authenticationState == AuthenticationState.ERROR_INPUT

    errorMessage = when (authenticationState) {
        AuthenticationState.ERROR_AUTHENTICATION ->
            stringResource(id = R.string.error_connection)

        AuthenticationState.ERROR_INPUT ->
            stringResource(id = CoreResource.string.error_empty_fields)

        else -> ""
    }

    DisposableEffect(context) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val insets = view.rootWindowInsets
            val keyboardHeight = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
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

    Surface(
        color = BackgroundVariant,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
        ) {
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
                    mailText = authenticationViewModel.mail,
                    mailOnValueChange = { authenticationViewModel.updateMailText(it) },
                    passwordText = authenticationViewModel.password,
                    passwordOnValueChange = { authenticationViewModel.updatePasswordText(it) },
                    errorMessage = errorMessage,
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    onClickConnectButton = {
                        keyboardController?.hide()
                        authenticationViewModel.login()
                    },
                    onClickRegistration = {
                        navController.navigate(Screen.FIRST_REGISTRATION_SCREEN.route)
                    },
                    isError = isError,
                    isEnable = authenticationState != AuthenticationState.LOADING
                )
            }

            if(authenticationState == AuthenticationState.LOADING) {
                LoadingScreen()
            }
        }
    }
}

@Composable
private fun TitleSection(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = CoreResource.drawable.ged_logo),
            contentDescription = stringResource(id = CoreResource.string.ged_logo_description),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
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
            color = Primary,
        )
    }
}

@Composable
private fun InputsSection(
    modifier: Modifier = Modifier,
    mailText: String,
    mailOnValueChange: (String) -> Unit,
    passwordText: String,
    passwordOnValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    errorMessage: String,
    isError: Boolean,
    isEnable: Boolean = true
) {
    Column(modifier = modifier) {
        OutlinedEmailInput(
            text = mailText,
            onValueChange = mailOnValueChange,
            keyboardActions = keyboardActions,
            isError = isError,
            isEnable = isEnable
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        OutlinedPasswordInput(
            text = passwordText,
            onValueChange = passwordOnValueChange,
            keyboardActions = keyboardActions,
            isError = isError,
            isEnable = isEnable
        )
        if (isError) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    mailText: String,
    mailOnValueChange: (String) -> Unit,
    passwordText: String,
    passwordOnValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    isError: Boolean,
    isEnable : Boolean = true,
    errorMessage: String,
    onClickConnectButton: () -> Unit,
    onClickRegistration: () -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        InputsSection(
            mailText = mailText,
            mailOnValueChange = mailOnValueChange,
            passwordText = passwordText,
            passwordOnValueChange = passwordOnValueChange,
            keyboardActions = keyboardActions,
            errorMessage = errorMessage,
            isError = isError,
            isEnable = isEnable
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        PrimaryLargeButton(
            text = stringResource(id = R.string.sign_in),
            onClick = onClickConnectButton,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.first_arrival),
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

            TextButton(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(24.dp),
                onClick = onClickRegistration,
                shape = ShapeDefaults.ExtraSmall
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

@Preview(widthDp = 360, heightDp = 740, showBackground = true)
@Composable
private fun AuthenticationScreenPreview() {
    var isLoading by remember { mutableStateOf(false) }
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mytext by remember { mutableStateOf("") }

    GedoiseTheme {
        Box(
            Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.aligned { size, space ->
                    size + (20 * space / 100)
                },
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TitleSection()

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                BottomSection(
                    mailText = mail,
                    mailOnValueChange = {
                        mail = it
                    },
                    passwordText = password,
                    passwordOnValueChange = {
                        password = it
                    },
                    errorMessage = "Veuillez remplir tous les champs",
                    onClickConnectButton = {
                        isLoading = !isLoading
                        mytext = mail
                    },
                    onClickRegistration = { },
                    keyboardActions = KeyboardActions.Default,
                    isError = false,
                    isEnable = !isLoading
                )
            }
            if(isLoading){
                LoadingScreen()
            }
        }
    }
}
