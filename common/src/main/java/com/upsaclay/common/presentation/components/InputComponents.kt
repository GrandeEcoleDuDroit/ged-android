package com.upsaclay.common.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

@Composable
fun SimpleOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    @StringRes errorMessage: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false
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

    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        isError = errorMessage != null,
        keyboardActions = keyboardActions,
        singleLine = true,
        supportingText = errorText,
        enabled = enabled,
        readOnly = readOnly
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit),
    textStyle: TextStyle = TextStyle.Default,
    enabled: Boolean = true
) {
    val colors: TextFieldColors = TextFieldDefaults.colors()
    val backgroundColor = MaterialTheme.colorScheme.background

    BasicTextField(
        modifier = modifier
            .background(backgroundColor)
            .padding(MaterialTheme.spacing.default),
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        cursorBrush = SolidColor(colors.cursorColor)
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
            contentPadding = PaddingValues(MaterialTheme.spacing.default)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun TransparentFocusedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit),
    textStyle: TextStyle = TextStyle.Default,
    enabled: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val lastVisibility = remember { mutableStateOf(false) }
    val isKeyboardVisible = WindowInsets.isImeVisible
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val backgroundColor = MaterialTheme.colorScheme.background

    LaunchedEffect(Unit) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible != lastVisibility.value && isKeyboardVisible) {
            textLayoutResult?.let {
                val cursorRect = it.getCursorRect(textFieldValue.selection.start)
                coroutineScope.launch {
                    bringIntoViewRequester.bringIntoView(cursorRect)
                }
            }
        }
    }

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .background(backgroundColor)
            .bringIntoViewRequester(bringIntoViewRequester),
        enabled = enabled,
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onValueChange(it.text)
        },
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        onTextLayout = {
            textLayoutResult = it
            val cursorRect = it.getCursorRect(textFieldValue.selection.start)
            coroutineScope.launch {
                bringIntoViewRequester.bringIntoView(cursorRect)
            }
        }
    ) { innerTextField ->
        val interactionSource = remember { MutableInteractionSource() }
        TextFieldDefaults.DecorationBox(
            value = textFieldValue.text,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = placeholder,
            contentPadding = PaddingValues(MaterialTheme.spacing.default)
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun OutlinedTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            SimpleOutlinedTextField(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
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
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Placeholder") }
        )
    }
}