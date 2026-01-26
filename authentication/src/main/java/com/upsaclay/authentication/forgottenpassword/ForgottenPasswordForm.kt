package com.upsaclay.authentication.forgottenpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.components.PrimaryButton

@Composable
fun ForgottenPasswordForm(
    onEmailChange : (String) -> Unit,
    onClick : () -> Unit
){
    Column() {
        OutlinedTextField(
            value = "",
            onValueChange = onEmailChange
        )
        PrimaryButton(
            modifier = Modifier,
            text = stringResource(R.string.send),
            onClick = onClick
        )
    }
}

/*
*
* PREVIEW
* */

@Preview
@Composable
fun ForgottenPasswordFormPreview() {
    ForgottenPasswordForm(
        onEmailChange = {},
        onClick = {}
    )

}