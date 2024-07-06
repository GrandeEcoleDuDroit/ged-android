package com.upsaclay.authentication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.core.R
import com.upsaclay.core.ui.theme.GedoiseColor
import com.upsaclay.core.ui.theme.GedoiseTheme

@Composable
fun EmailInput(
    text: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        label = { Text(text = stringResource(id = R.string.email)) },
        onValueChange = onValueChange,
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
    var passwordVisible by remember { mutableStateOf(false) }
    val icon: Painter
    val contentDescription: String
    if (!passwordVisible) {
        icon = painterResource(id = R.drawable.ic_visibility)
        contentDescription = stringResource(
            id = com.upsaclay.authentication.R.string.show_password_icon_description
        )
    } else {
        icon = painterResource(id = R.drawable.ic_visibility_off)
        contentDescription = stringResource(
            id = com.upsaclay.authentication.R.string.hide_password_icon_description
        )
    }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        label = { Text(text = stringResource(id = com.upsaclay.authentication.R.string.password)) },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                tint = GedoiseColor.BlackIconColor
            )
        },
        visualTransformation =
        if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        isError = isError,
        singleLine = true
    )
}

@Preview
@Composable
fun EmailInputPreview(){
    GedoiseTheme {

    }
}
@Preview(showBackground = true, widthDp = 360, heightDp = 220)
@Composable
fun PasswordInputPreview(){
    GedoiseTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            EmailInput(
                text ="",
                onValueChange = {}
            )
            Spacer(modifier = Modifier.height(20.dp))
            PasswordInput(
                text = "",
                onValueChange = {}
            )
        }
    }
}