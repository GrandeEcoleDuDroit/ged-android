package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.DateUtils
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.message.R
import com.upsaclay.message.domain.fixtures.conversationFixture
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.fixtures.messagesFixture
import com.upsaclay.message.presentation.chat.ChatViewModel.MessageEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.Duration

@Composable
internal fun MessageFeed(
    modifier: Modifier = Modifier,
    messages: Flow<PagingData<Message>>,
    interlocutor: User,
    newMessageEvent: MessageEvent.NewMessage?,
    onErrorSentMessageClick: (Message) -> Unit,
    onReceivedMessageLongClick: (Message) -> Unit,
    onInterlocutorClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showNewMessageIndicator by remember { mutableStateOf(false) }
    val isAtBottom = remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }
    val messageItems = messages.collectAsLazyPagingItems()

    LaunchedEffect(newMessageEvent) {
        newMessageEvent?.let { event ->
            when {
                listState.firstVisibleItemIndex <= 1 -> listState.animateScrollToItem(0)

                listState.firstVisibleItemIndex > 1 && event.message.senderId == interlocutor.id ->
                    showNewMessageIndicator = true
            }
        }
    }

    if (isAtBottom.value) {
        showNewMessageIndicator = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag(stringResource(R.string.chat_screen_lazy_column_item_tag)),
            reverseLayout = true,
            state = listState
        ) {
            items(
                count = messageItems.itemCount,
                key = messageItems.itemKey { it.id },
                contentType = messageItems.itemContentType { "MessageFeed" }
            ) { index ->
                val message = messageItems[index] ?: return@items
                val previousMessage = messageItems.takeIf { index + 1 < messageItems.itemCount }?.get(index + 1)
                val condition = MessageCondition(
                    message = message,
                    interlocutor = interlocutor,
                    messageSize = messageItems.itemCount,
                    index = index,
                    previousMessage = previousMessage
                )

                MessageItem(
                    modifier = Modifier.padding(top = messageTopPadding(condition)),
                    message = message,
                    condition = condition,
                    interlocutor = interlocutor,
                    index = index,
                    onErrorMessageClick = onErrorSentMessageClick,
                    onLongClick = onReceivedMessageLongClick,
                    onInterlocutorClick = onInterlocutorClick
                )

                if (condition.isOldestMessage || !condition.sameDay) {
                    val topPadding = if (condition.isOldestMessage) {
                        dimensionResource(com.upsaclay.common.R.dimen.default_padding)
                    } else {
                        dimensionResource(com.upsaclay.common.R.dimen.medium_large_padding)
                    }

                    Text(
                        modifier = Modifier
                            .padding(top = topPadding, bottom = dimensionResource(com.upsaclay.common.R.dimen.medium_large_padding))
                            .fillMaxWidth(),
                        text = DateUtils.formatDayMonthYear(message.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (showNewMessageIndicator) {
            NewMessageIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .testTag(stringResource(R.string.chat_screen_message_indicator_tag)),
                onClick = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }
    }
}

@Composable
private fun MessageItem(
    modifier: Modifier = Modifier,
    message: Message,
    condition: MessageCondition,
    interlocutor: User,
    index: Int,
    onErrorMessageClick: (Message) -> Unit,
    onLongClick: (Message) -> Unit,
    onInterlocutorClick: () -> Unit
) {
    if (condition.isSender) {
        SentMessageItem(
            modifier = modifier
                .testTag(stringResource(R.string.chat_screen_send_message_item_tag) + index),
            message = message,
            showSeen = condition.showSeenMessage,
            clickEnabled = message.state == MessageState.ERROR,
            onClick = { onErrorMessageClick(message) }
        )
    } else {
        ReceivedMessageItem(
            modifier = modifier
                .testTag(stringResource(R.string.chat_screen_receive_message_item_tag) + index),
            message = message,
            displayProfilePicture = condition.displayProfilePicture,
            profilePictureUrl = interlocutor.profilePictureUrl,
            onLongClick = { onLongClick(message) },
            onInterlocutorClick = onInterlocutorClick
        )
    }
}

private data class MessageCondition(
    private val message: Message,
    private val interlocutor: User,
    private val messageSize: Int,
    private val index: Int,
    private val previousMessage: Message?
) {
    val isSender = message.senderId != interlocutor.id
    val isOldestMessage = index == messageSize - 1
    val previousSenderId = previousMessage?.senderId ?: ""
    val sameSender = message.senderId == previousSenderId
    val showSeenMessage = index == 0 && isSender && message.seen
    val sameTime = previousMessage?.let {
        Duration.between(it.date, message.date).toMinutes() <= 1
    } ?: false
    val sameDay = previousMessage?.let {
        Duration.between(it.date, message.date).toDays() <= 1L
    } ?: false
    val displayProfilePicture = !sameTime || !sameSender
}

@Composable
private fun messageTopPadding(condition: MessageCondition): Dp {
    val small = condition.sameSender && condition.sameTime
    val smallMedium = condition.sameSender && !condition.sameTime && condition.sameDay
    val zero = !condition.sameDay

    return when {
        small -> 2.dp
        smallMedium -> dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)
        zero -> dimensionResource(com.upsaclay.common.R.dimen.default_padding)
        else -> dimensionResource(com.upsaclay.common.R.dimen.medium_padding)
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MessageFeedPreview() {
    GedoiseTheme {
        Surface {
            MessageFeed(
                modifier = Modifier.rootMediumPadding(),
                messages = flowOf(PagingData.from(messagesFixture)),
                interlocutor = conversationFixture.interlocutor,
                newMessageEvent = null,
                onErrorSentMessageClick = {},
                onReceivedMessageLongClick = {},
                onInterlocutorClick = {}
            )
        }
    }
}