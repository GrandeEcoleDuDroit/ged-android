package com.upsaclay.common.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.outlinedTextFieldColor
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun SimpleOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    val errorText: (@Composable () -> Unit)? = errorMessage?.let {
        {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        isError = errorMessage != null,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        supportingText = errorText,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        readOnly = readOnly,
        minLines = minLines
    )
}

@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    @StringRes errorMessage: Int? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine : Boolean = true,
    minLines: Int = 1,
) {
    val errorText: (@Composable () -> Unit)? = errorMessage?.let {
        {
            Text(
                text = stringResource(errorMessage),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    TextField(
        modifier = modifier,
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        isError = errorMessage != null,
        keyboardActions = keyboardActions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        supportingText = errorText,
        enabled = enabled,
        readOnly = readOnly,
        colors = MaterialTheme.colorScheme.outlinedTextFieldColor,
        minLines = minLines
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit),
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences
    ),
    minLines: Int = 1,
    singleLine: Boolean = false,
    enabled: Boolean = true
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    BasicTextField(
        modifier = modifier.background(backgroundColor),
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        minLines = minLines,
        cursorBrush = SolidColor(TextFieldDefaults.colors().cursorColor)
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = placeholder,
            contentPadding = PaddingValues()
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun SimpleOutlinedTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            SimpleOutlinedTextField(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
                value = text,
                label = "Label",
                onValueChange = { text = it }
            )
        }
    }
}

@PhonePreviews
@Composable
private fun SimpleTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            SimpleTextField(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
                value = text,
                label = "Label",
                onValueChange = { text = it }
            )
        }
    }
}

@Preview
@Composable
private fun TransparentTextFieldPreview() {
    var value by remember { mutableStateOf("") }

    GedoiseTheme {
        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { value = it },
            placeholder = { Text("Placeholder") }
        )
    }
}