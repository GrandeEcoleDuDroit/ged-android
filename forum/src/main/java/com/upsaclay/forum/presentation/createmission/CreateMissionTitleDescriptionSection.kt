package com.upsaclay.forum.presentation.createmission

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.extension.bringIntoView
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.titleMediumLarge
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateMissionTitleDescriptionSection(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    scrollState: ScrollState
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    SelectionContainer {
        Column(
            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            TransparentTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoView(scrollState)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                value = title,
                onValueChange = onTitleChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.title),
                        style = MaterialTheme.typography.titleMediumLarge,
                        color = TextFieldDefaults.colors().unfocusedPlaceholderColor
                    )
                },
                textStyle = MaterialTheme.typography.titleMediumLarge
            )

            TransparentTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.description),
                        color = TextFieldDefaults.colors().unfocusedPlaceholderColor
                    )
                },
                minLines = 4,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Phones
@Composable
private fun CreateMissionTitleDescriptionSectionPreview() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            CreateMissionTitleDescriptionSection(
                title = title,
                description = description,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                scrollState = ScrollState(0)
            )
        }
    }
}