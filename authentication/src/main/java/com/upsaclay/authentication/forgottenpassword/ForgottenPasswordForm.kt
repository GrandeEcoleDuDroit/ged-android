package com.upsaclay.authentication.forgottenpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.upsaclay.common.R as commonR
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun ForgottenPasswordForm(
    onEmailChange : (String) -> Unit,
    onClick : () -> Unit,
    email : String,
    onEmailError : Int?
){
    Column() {
        SimpleOutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = stringResource(commonR.string.email),
            errorMessage = onEmailError?.let { stringResource(it) }
        )
        Spacer(modifier = Modifier.height(dimensionResource(commonR.dimen.extra_large_padding)))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.send),
            onClick = onClick
        )
    }
}

/*
*
* PREVIEW
* */

@PhonePreviews
@Composable
fun ForgottenPasswordFormPreview() {
    GedoiseTheme {
        ForgottenPasswordForm(
            onEmailChange = {},
            onClick = {},
            email = "",
            onEmailError = null

        )
    }

}