package com.upsaclay.authentication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.upsaclay.authentication.R
import com.upsaclay.authentication.data.AuthenticationState
import com.upsaclay.core.ui.components.ErrorText
import com.upsaclay.core.ui.components.InfiniteCircularProgressIndicator
import com.upsaclay.core.ui.components.PrimaryLargeButton
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.ui.theme.PrimaryColor
import com.upsaclay.core.ui.theme.SecondaryBackgroundColor
import com.upsaclay.core.R as CoreResource

@Composable
fun AuthenticationScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    authenticationViewModel: AuthenticationViewModel = viewModel()
) {
    val authenticationState by authenticationViewModel.authenticationState.collectAsState()
//    if(authenticationState == AuthenticationState.IS_AUTHENTICATED){
//        navController.navigate(Screen.Home.route)
//    }
    var errorMessage by remember { mutableStateOf("") }
    errorMessage = when (authenticationState) {
        AuthenticationState.ERROR_AUTHENTICATION ->
            stringResource(id = R.string.error_connection)

        AuthenticationState.ERROR_INPUT ->
            stringResource(id = CoreResource.string.error_empty_fields)

        else -> ""
    }
    Surface(
        color = SecondaryBackgroundColor,
        modifier = modifier.fillMaxSize()
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(16.dp)
        ) {
            TitleSection()
            BottomSection(
                usernameText = authenticationViewModel.username,
                usernameOnValueChange = { authenticationViewModel.updateUsername(it) },
                passwordText = authenticationViewModel.password,
                passwordOnValueChange = { authenticationViewModel.updatePassword(it) },
                errorMessage = errorMessage,
                authenticationState = authenticationState,
                onClickButton = { authenticationViewModel.loginWithParisSaclay() },
                isLoading = authenticationState == AuthenticationState.LOADING
            )
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.offset(y = (-80).dp)
    ) {
        Image(
            painter = painterResource(id = CoreResource.drawable.ged_logo),
            contentDescription = stringResource(id = CoreResource.string.ged_logo_description),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
        )
        Text(
            text = stringResource(id = R.string.welcome_text),
            style = MaterialTheme.typography.titleSmall,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.presentation_text),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = PrimaryColor,
        )
    }
}

@Composable
fun InputsSection(
    usernameText: String,
    usernameOnValueChange: (String) -> Unit,
    passwordText: String,
    passwordOnValueChange: (String) -> Unit,
    errorMessage: String,
    authenticationState: AuthenticationState,
) {
    val isError = authenticationState == AuthenticationState.ERROR_AUTHENTICATION ||
            authenticationState == AuthenticationState.ERROR_INPUT
    Column {
        EmailInput(
            text = usernameText,
            onValueChange = usernameOnValueChange,
            isError = isError
        )
        Spacer(modifier = Modifier.height(5.dp))
        PasswordInput(
            text = passwordText,
            onValueChange = passwordOnValueChange,
            isError = isError
        )
        if (isError) {
            Spacer(modifier = Modifier.height(5.dp))
            ErrorText(
                text = errorMessage,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}

@Composable
fun BottomSection(
    usernameText: String,
    usernameOnValueChange: (String) -> Unit,
    passwordText: String,
    passwordOnValueChange: (String) -> Unit,
    errorMessage: String,
    authenticationState: AuthenticationState,
    onClickButton: () -> Unit,
    isLoading: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = (-20).dp)
    ) {
        InputsSection(
            usernameText = usernameText,
            usernameOnValueChange = usernameOnValueChange,
            passwordText = passwordText,
            passwordOnValueChange = passwordOnValueChange,
            errorMessage = errorMessage,
            authenticationState = authenticationState
        )
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryLargeButton(
            text = stringResource(id = R.string.connect),
            onClick = onClickButton,
            modifier = Modifier.fillMaxWidth()
        )
        if (isLoading) {
            InfiniteCircularProgressIndicator()
        }
    }
}

@Composable
fun EmailInput(
    text: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        label = { Text(text = stringResource(id = CoreResource.string.email)) },
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = stringResource(
                    id = R.string.email_icon_description
                )
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = isError,
        singleLine = true
    )
}

@Composable
fun PasswordInput(
    text: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        label = { Text(text = stringResource(id = R.string.password)) },
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = stringResource(
                    id = R.string.password_icon_description
                )
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        isError = isError,
        singleLine = true
    )
}

@Preview(widthDp = 360, heightDp = 840, showBackground = true)
@Composable
fun AuthenticationScreenPreview() {
    var isLoading by remember { mutableStateOf(false) }
    GedoiseTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            TitleSection()
            BottomSection(
                usernameText = "",
                usernameOnValueChange = {},
                passwordText = "",
                passwordOnValueChange = {},
                authenticationState = AuthenticationState.UNAUTHENTICATED,
                errorMessage = "",
                onClickButton = { isLoading = !isLoading },
                isLoading = isLoading
            )
        }
    }
}
