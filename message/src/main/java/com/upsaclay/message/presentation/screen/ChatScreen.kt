package com.upsaclay.message.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.presentation.components.TransparentFocusedTextField
import com.upsaclay.common.presentation.theme.GedoiseColor
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.userFixture
import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.presentation.components.ChatTopBar
import com.upsaclay.message.presentation.components.ReceiveMessageItem
import com.upsaclay.message.presentation.components.SentMessageItem
import com.upsaclay.message.presentation.viewmodel.ChatViewModel
import com.upsaclay.message.utils.conversationFixture
import com.upsaclay.message.utils.messagesFixture
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = koinViewModel(),
) {
//    val conversation = chatViewModel.conversation.collectAsState(initial = null).value
    val conversation = chatViewModel.conversation
//    val user = chatViewModel.user.collectAsState(initial = null).value
    val user = userFixture
    val text = chatViewModel.text

    conversation?.let {
        user?.let {
            Scaffold(
                topBar = {
                    ChatTopBar(navController = navController, interlocutor = conversation.interlocutor)
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(
                        top = innerPadding.calculateTopPadding(),
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.small
                    )
                ) {
                    MessageSection(
                        modifier = Modifier.weight(1f),
                        messages = conversation.messages,
                        currentUser = user
                    )

                    TransparentFocusedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        defaultValue = text,
                        onValueChange = { chatViewModel.updateText(it) },
                        placeholder = { Text("Placeholder") },
                        backgroundColor = GedoiseColor.LightGray,
                        shape = ShapeDefaults.ExtraLarge,
                        padding = MaterialTheme.spacing.smallMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageSection(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    currentUser: User
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        reverseLayout = true,
    ) {
        itemsIndexed(messages) { index, message ->
            val sameSender = if (index > 0) {
                message.sender == messages[index - 1].sender
            } else {
                false
            }

            val spacing = if (sameSender) {
                MaterialTheme.spacing.extraSmall
            } else {
                MaterialTheme.spacing.smallMedium
            }

            val isCurrentUserSender = message.sender.id == currentUser.id

            Spacer(modifier = Modifier.height(spacing))

            if (isCurrentUserSender) {
                SentMessageItem(text = message.text)
            } else {
                if (index == 0 || !sameSender) {
                    ReceiveMessageItem(message = message, displayProfilePicture = true)
                } else {
                    ReceiveMessageItem(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.extraLarge),
                        message = message,
                        displayProfilePicture = false
                    )
                }
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun ChatScreenPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        Scaffold(
            topBar = {
                ChatTopBar(
                    navController = rememberNavController(),
                    interlocutor = conversationFixture.interlocutor
                )
            }
        ) { innerPadding ->
            Column {
                Column(
                    modifier = Modifier
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            start = MaterialTheme.spacing.medium,
                            end = MaterialTheme.spacing.medium,
                            bottom = MaterialTheme.spacing.small
                        )
                ) {
                    MessageSection(
                        modifier = Modifier.weight(1f),
                        messages = messagesFixture,
                        currentUser = userFixture
                    )

                    TransparentFocusedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        defaultValue = text,
                        onValueChange = { text = it },
                        placeholder = { Text("Message") },
                        backgroundColor = GedoiseColor.LightGray,
                        shape = ShapeDefaults.ExtraLarge,
                        padding = MaterialTheme.spacing.smallMedium
                    )
                }
            }
        }
    }
}