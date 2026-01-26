package com.upsaclay.authentication.forgottenpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.components.BackTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgottenPasswordDestination(
    onBackClick: () -> Unit,
) {
    ForgottenPasswordScreen(
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgottenPasswordScreen(
    onBackClick: () -> Unit,
    viewModel: ForgottenPasswordViewModel = koinViewModel()
) {
    Column() {
        BackTopBar(
            onBackClick = onBackClick,
            title = stringResource(R.string.forgotten_password)
        )
        ForgottenPasswordForm(
            onEmailChange = viewModel::onEmailChange,
            onClick = viewModel::onClick
        )
    }


}

/*
* PREVIEW
* */

@Preview
@Composable
fun ForgottenPasswordScreenPreview() {
    ForgottenPasswordScreen(
        onBackClick = {}
    )
}