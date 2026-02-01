package com.upsaclay.authentication.presentation.forgottenpassword

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.upsaclay.authentication.R
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.LoadingButton
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.common.R as commonR

@Composable
fun ForgottenPasswordForm(
    email : String,
    loading: Boolean,
    @StringRes emailError: Int?,
    @StringRes errorMessage: Int?,
    onEmailChange : (String) -> Unit,
    onSendPasswordResetClick : () -> Unit
){
    Column(
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        Text(
            text = stringResource(id = R.string.enter_email),
            style = MaterialTheme.typography.titleMedium
        )

        SimpleOutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = stringResource(commonR.string.email),
            errorMessage = emailError?.let { stringResource(it) }
        )

        errorMessage?.let {
            Text(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                    .align(Alignment.Start),
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LoadingButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.send),
            loading = loading,
            onClick = onSendPasswordResetClick
        )
    }
}

/*
==================================================================
                                PREVIEW
====================================================================
*/

@PhonePreviews
@Composable
fun ForgottenPasswordFormPreview() {
    var email by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            ForgottenPasswordForm(
                email = email,
                loading = false,
                emailError = null,
                errorMessage = null,
                onEmailChange = { email = it },
                onSendPasswordResetClick = {},
            )
        }
    }
}
