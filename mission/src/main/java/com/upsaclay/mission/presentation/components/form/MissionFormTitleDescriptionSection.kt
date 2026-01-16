package com.upsaclay.mission.presentation.components.form

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.presentation.MissionPresentationUtils.contentStyle
import com.upsaclay.mission.presentation.MissionPresentationUtils.titleStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MissionFormTitleDescriptionSection(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    SelectionContainer {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
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
                value = title,
                onValueChange = onTitleChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.title),
                        style = titleStyle,
                        color = MaterialTheme.colorScheme.inputForeground
                    )
                },
                textStyle = titleStyle
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
                        style = contentStyle,
                        color = MaterialTheme.colorScheme.inputForeground
                    )
                },
                minLines = 4,
                textStyle = contentStyle
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MissionFormTitleDescriptionSectionPreview() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            MissionFormTitleDescriptionSection(
                title = title,
                description = description,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it }
            )
        }
    }
}