package com.upsaclay.authentication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.upsaclay.authentication.R
import com.upsaclay.authentication.data.AuthenticationState
import com.upsaclay.core.data.Screen
import com.upsaclay.core.ui.components.ErrorText
import com.upsaclay.core.ui.components.InfiniteCircularProgressIndicator
import com.upsaclay.core.ui.components.PrimaryLargeButton
import com.upsaclay.core.ui.theme.GedoiseColor.PrimaryColor
import com.upsaclay.core.ui.theme.GedoiseColor.SecondaryBackgroundColor
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.R as CoreResource

@Composable
fun AuthenticationScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    authenticationViewModel: AuthenticationViewModel = viewModel()
) {
    val authenticationState by authenticationViewModel.authenticationState.collectAsState()
    if(authenticationState == AuthenticationState.AUTHENTICATED){
        navController.navigate(Screen.HOME.route)
    }
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
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxHeight(0.9f)
            ) {
                TitleSection()
                BottomSection(
                    mailText = authenticationViewModel.username,
                    mailOnValueChange = { authenticationViewModel.updateUsername(it) },
                    passwordText = authenticationViewModel.password,
                    passwordOnValueChange =
                    { authenticationViewModel.updatePassword(it) },
                    errorMessage = errorMessage,
                    authenticationState = authenticationState,
                    onClickButton = { authenticationViewModel.loginWithParisSaclay() },
                )
            }
            if (authenticationState == AuthenticationState.LOADING) {
                InfiniteCircularProgressIndicator()
            }
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = CoreResource.drawable.ged_logo),
            contentDescription = stringResource(id = CoreResource.string.ged_logo_description),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.welcome_text),
            style = MaterialTheme.typography.titleSmall,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
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
    mailText: String,
    mailOnValueChange: (String) -> Unit,
    passwordText: String,
    passwordOnValueChange: (String) -> Unit,
    errorMessage: String,
    authenticationState: AuthenticationState,
    onClickButton: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InputsSection(
            usernameText = mailText,
            usernameOnValueChange = mailOnValueChange,
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
    }
}

@Preview(widthDp = 360, heightDp = 740, showBackground = true)
@Composable
fun AuthenticationScreenPreview() {
    var isLoading by remember { mutableStateOf(false) }
    var mail by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var mytext by remember {
        mutableStateOf("")
    }
    GedoiseTheme {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(0.9f)
            ) {
                TitleSection()
                BottomSection(
                    mailText = mail,
                    mailOnValueChange = {
                        mail = it
                    },
                    passwordText = password,
                    passwordOnValueChange = {
                        password = it
                    },
                    authenticationState = AuthenticationState.UNAUTHENTICATED,
                    errorMessage = "",
                    onClickButton = {
                        isLoading = !isLoading
                        mytext = mail
                    },
                )
            }
            if (isLoading) {
                InfiniteCircularProgressIndicator()
            }
            Text(text = mytext)
        }
    }
}
