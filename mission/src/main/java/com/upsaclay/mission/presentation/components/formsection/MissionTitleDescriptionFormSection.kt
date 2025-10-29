package com.upsaclay.mission.presentation.components.formsection

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
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.missionContent
import com.upsaclay.common.presentation.theme.missionTitle
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MissionTitleDescriptionFormSection(
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
            verticalArrangement = Arrangement.smallSpacing()
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
                        style = MaterialTheme.typography.missionTitle,
                        color = TextFieldDefaults.colors().unfocusedPlaceholderColor
                    )
                },
                textStyle = MaterialTheme.typography.missionTitle
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
                textStyle = MaterialTheme.typography.missionContent
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateMissionTitleDescriptionSectionPreview() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            MissionTitleDescriptionFormSection(
                title = title,
                description = description,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                scrollState = ScrollState(0)
            )
        }
    }
}