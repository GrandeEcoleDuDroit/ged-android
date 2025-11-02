package com.upsaclay.news.presentation.announcement.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.hintText
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R

@Composable
fun CreateAnnouncementInputs(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit
) {
    SelectionContainer(modifier = modifier) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            AnnouncementTitleInput(
                title = title,
                onTitleChange = onTitleChange,
                focused = true
            )

            AnnouncementContentInput(
                content = content,
                onContentChange = onContentChange
            )
        }
    }
}

@Composable
fun EditAnnouncementInputs(
    modifier: Modifier = Modifier,
    title: String,
    content: TextFieldValue,
    onTitleChange: (String) -> Unit,
    onContentChange: (TextFieldValue) -> Unit
) {
    SelectionContainer(modifier = modifier) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            AnnouncementTitleInput(
                title = title,
                onTitleChange = onTitleChange
            )

            AnnouncementContentInput(
                content = content,
                onContentChange = onContentChange
            )
        }
    }
}

@Composable
private fun AnnouncementTitleInput(
    title: String,
    onTitleChange: (String) -> Unit,
    focused: Boolean = false
) {
    val textStyle = MaterialTheme.typography.titleLarge
    val focusRequester = remember { FocusRequester() }
    val placeholder: @Composable () -> Unit = {
        Text(
            text = stringResource(id = R.string.title_field_entry),
            style = textStyle,
            color = MaterialTheme.colorScheme.hintText
        )
    }

    LaunchedEffect(Unit) {
        if (focused) {
            focusRequester.requestFocus()
        }
    }

    TransparentTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = title,
        placeholder = placeholder,
        onValueChange = onTitleChange,
        textStyle = textStyle
    )
}

@Composable
private fun AnnouncementContentInput(
    content: String,
    onContentChange: (String) -> Unit
) {
    val textStyle = MaterialTheme.typography.bodyLarge
    val placeholder: @Composable () -> Unit = {
        Text(
            text = stringResource(id = R.string.content_field_entry),
            style = textStyle,
            color = MaterialTheme.colorScheme.hintText
        )
    }

    TransparentTextField(
        modifier = Modifier.fillMaxWidth(),
        value = content,
        placeholder = placeholder,
        onValueChange = onContentChange,
        textStyle = textStyle
    )
}

@Composable
private fun AnnouncementContentInput(
    content: TextFieldValue,
    onContentChange: (TextFieldValue) -> Unit
) {
    val textStyle = MaterialTheme.typography.bodyLarge
    val focusRequester = remember { FocusRequester() }
    val placeholder: @Composable () -> Unit = {
        Text(
            text = stringResource(id = R.string.content_field_entry),
            style = textStyle,
            color = MaterialTheme.colorScheme.hintText
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TransparentTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = content,
        placeholder = placeholder,
        onValueChange = onContentChange,
        textStyle = textStyle
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AnnouncementInputPreview() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            CreateAnnouncementInputs(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                title = title,
                content = content,
                onTitleChange = { title = it },
                onContentChange = { content = it }
            )
        }
    }
}