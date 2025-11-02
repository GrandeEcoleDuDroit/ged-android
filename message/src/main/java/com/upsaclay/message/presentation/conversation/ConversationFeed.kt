package com.upsaclay.message.presentation.conversation

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationsUIFixture
import com.upsaclay.message.domain.entity.ConversationUi

@Composable
fun ConversationFeed(
    modifier: Modifier = Modifier,
    conversations: List<ConversationUi>,
    onClick: (ConversationUi) -> Unit,
    onLongClick: (ConversationUi) -> Unit,
    onCreateClick: () -> Unit
) {
    LazyColumn(modifier = modifier) {
        if (conversations.isEmpty()) {
            item { EmptyConversationText(onCreateClick) }
        } else {
            items(conversations.size) { index ->
                val conversation = conversations[index]
                ConversationItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(id = R.string.conversation_screen_conversation_item_tag)),
                    conversationUi = conversation,
                    onClick = { onClick(conversation) },
                    onLongClick = { onLongClick(conversation) }
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
            color = MaterialTheme.colorScheme.previewText,
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

@Phones
@Composable
private fun ConversationFeedPreview() {
    GedoiseTheme {
        Surface {
            ConversationFeed(
                conversations = conversationsUIFixture,
                onClick = {},
                onLongClick = {},
                onCreateClick = {}
            )
        }
    }
}