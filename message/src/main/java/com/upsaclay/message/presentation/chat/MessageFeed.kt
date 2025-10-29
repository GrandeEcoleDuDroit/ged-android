package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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
import com.upsaclay.common.extension.mediumPadding
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.FormatLocalDateTimeHelper
import com.upsaclay.common.utils.Phones
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.messageFixture
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
                val previousMessage = messageItems.takeIf {
                    index + 1 < messageItems.itemCount
                }?.get(index + 1)
                val condition = MessageCondition(
                    message = message,
                    interlocutor = interlocutor,
                    messageSize = messageItems.itemCount,
                    index = index,
                    previousMessage = previousMessage
                )

                if (condition.isSender) {
                    SentMessageItem(
                        modifier = Modifier
                            .testTag(stringResource(R.string.chat_screen_send_message_item_tag) + index),
                        message = message,
                        showSeen = condition.showSeenMessage,
                        clickEnabled = message.state == MessageState.ERROR,
                        onClick = { onErrorSentMessageClick(message) }
                    )
                } else {
                    ReceivedMessageItem(
                        modifier = Modifier
                            .testTag(stringResource(R.string.chat_screen_receive_message_item_tag) + index),
                        message = message,
                        displayProfilePicture = condition.displayProfilePicture,
                        profilePictureUrl = interlocutor.profilePictureUrl,
                        onLongClick = { onReceivedMessageLongClick(message) },
                        onInterlocutorClick = onInterlocutorClick
                    )
                }

                if (condition.isFirstMessage || !condition.sameDay) {
                    val topPadding = if (condition.isFirstMessage) {
                        dimensionResource(com.upsaclay.common.R.dimen.default_padding)
                    } else {
                        dimensionResource(com.upsaclay.common.R.dimen.medium_large_padding)
                    }

                    Text(
                        modifier = Modifier
                            .padding(top = topPadding, bottom = dimensionResource(com.upsaclay.common.R.dimen.medium_large_padding))
                            .fillMaxWidth(),
                        text = FormatLocalDateTimeHelper.formatDayMonthYear(message.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.previewText,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Spacer(
                        modifier = Modifier.height(
                            messagePadding(condition.sameSender, condition.sameTime)
                        )
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

private data class MessageCondition(
    private val message: Message,
    private val interlocutor: User,
    private val messageSize: Int,
    private val index: Int,
    private val previousMessage: Message?
) {
    val isSender = message.senderId != interlocutor.id
    val isFirstMessage = index == messageSize - 1
    val isLastMessage = index == 0
    val previousSenderId = previousMessage?.senderId ?: ""
    val sameSender = previousSenderId == message.senderId
    val showSeenMessage = isLastMessage && isSender && message.seen
    val sameTime = previousMessage?.let {
        Duration.between(it.date, message.date).toMinutes() < 2L
    } ?: false
    val sameDay = previousMessage?.let {
        Duration.between(it.date, message.date).toDays() < 2L
    } ?: false
    val displayProfilePicture = !sameTime || isFirstMessage || !sameSender
}

@Composable
private fun messagePadding(sameSender: Boolean, sameTime: Boolean): Dp =
    if (sameSender && sameTime) 1.dp else dimensionResource(com.upsaclay.common.R.dimen.small_padding)

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun MessageFeedPreview() {
    GedoiseTheme {
        MessageFeed(
            modifier = Modifier.mediumPadding(),
            messages = flowOf(PagingData.from(listOf(messageFixture))),
            interlocutor = conversationFixture.interlocutor,
            newMessageEvent = null,
            onErrorSentMessageClick = {},
            onReceivedMessageLongClick = {},
            onInterlocutorClick = {}
        )
    }
}