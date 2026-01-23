package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.supportingText
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.fixtures.conversationsUiFixture

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationFeed(
    modifier: Modifier = Modifier,
    conversationsUi: List<ConversationUi>,
    onClick: (ConversationUi) -> Unit,
    onLongClick: (ConversationUi) -> Unit,
    onCreateClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    LazyColumn(modifier = modifier) {
        if (conversationsUi.isEmpty()) {
            item { EmptyConversationText(onCreateClick) }
        } else {
            items(conversationsUi.size) { index ->
                val conversationUi = conversationsUi[index]
                ConversationItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { onClick(conversationUi) },
                            onLongClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onLongClick(conversationUi)
                            }
                        )
                        .testTag(stringResource(id = R.string.conversation_screen_conversation_item_tag)),
                    conversationUi = conversationUi
                )
            }
        }
    }
}

@Composable
private fun EmptyConversationText(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.conversation_screen_empty_conversation_text_tag)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.start_conversation),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.supportingText,
            textAlign = TextAlign.Center
        )

        TextButton(
            contentPadding = PaddingValues(
                top = dimensionResource(com.upsaclay.common.R.dimen.default_padding),
                bottom = dimensionResource(com.upsaclay.common.R.dimen.default_padding),
                start = ButtonDefaults.TextButtonContentPadding.calculateLeftPadding(LayoutDirection.Rtl),
                end = ButtonDefaults.TextButtonContentPadding.calculateRightPadding(LayoutDirection.Rtl)
            ),
            modifier = Modifier.height(30.dp),
            shape = ShapeDefaults.ExtraSmall,
            onClick = onCreateClick
        ) {
            Text(
                text = stringResource(id = R.string.new_conversation)
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
private fun ConversationFeedPreview() {
    GedoiseTheme {
        Surface {
            ConversationFeed(
                conversationsUi = conversationsUiFixture,
                onClick = {},
                onLongClick = {},
                onCreateClick = {}
            )
        }
    }
}