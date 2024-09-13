package com.upsaclay.news.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.upsaclay.common.presentation.theme.spacing
import kotlinx.coroutines.android.awaitFrame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit),
    textStyle: TextStyle,
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        )
    ) { innerTextField ->
        val interactionSource = remember { MutableInteractionSource() }
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = placeholder,
            contentPadding = PaddingValues(MaterialTheme.spacing.default)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentFocusedTextField(
    modifier: Modifier = Modifier,
    defaultValue: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit),
    textStyle: TextStyle,
) {
    val focusRequester = remember{ FocusRequester() }
    val textFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                text = defaultValue,
                selection = TextRange(defaultValue.length)
            )
        )
    }

    LaunchedEffect(Unit) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    BasicTextField(
        modifier = modifier.focusRequester(focusRequester),
        value = textFieldValue.value,
        onValueChange = { newValue ->
            textFieldValue.value = newValue
            onValueChange(newValue.text)
        },
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        )
    ) { innerTextField ->
        val interactionSource = remember { MutableInteractionSource() }
        TextFieldDefaults.DecorationBox(
            value = textFieldValue.value.text,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = placeholder,
            contentPadding = PaddingValues(MaterialTheme.spacing.default)
        )
    }
}