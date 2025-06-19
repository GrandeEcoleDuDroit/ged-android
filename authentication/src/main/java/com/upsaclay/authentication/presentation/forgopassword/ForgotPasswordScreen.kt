package com.upsaclay.authentication.presentation.forgopassword

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.theme.GedoiseTheme

@Composable
fun ForgotPasswordScreen(
    email: String
) {

}


@Composable
@Preview
fun ForgotPasswordPreview() {
    val email by remember { mutableStateOf("") }

    GedoiseTheme {
        ForgotPasswordScreen(
            email = email
        )
    }
}