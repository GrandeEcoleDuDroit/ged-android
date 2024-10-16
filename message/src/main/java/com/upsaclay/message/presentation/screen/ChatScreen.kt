package com.upsaclay.message.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.presentation.components.OverlayLoadingScreen
import com.upsaclay.common.presentation.components.TransparentFocusedTextField
import com.upsaclay.common.presentation.theme.GedoiseColor
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.userFixture
import com.upsaclay.message.R
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
    interlocutorId: Int,
    navController: NavController,
    chatViewModel: ChatViewModel = koinViewModel()
) {
    val conversation = chatViewModel.conversation.collectAsState(null).value
    val currentUser = chatViewModel.currentUser.collectAsState(null).value

    LaunchedEffect(Unit) {
        chatViewModel.getConversation(interlocutorId)
    }

    if (conversation != null && currentUser != null) {
        Scaffold(
            topBar = {
                ChatTopBar(
                    navController = navController,
                    interlocutor = conversation.interlocutor
                )
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
                DisplayMessageSection(
                    modifier = Modifier.weight(1f),
                    messages = conversation.messages,
                    currentUser = currentUser,
                    interlocutor = conversation.interlocutor
                )

                MessageInputSection(
                    text = chatViewModel.messageToSend,
                    onTextChange = { chatViewModel.updateMessageToSend(it) },
                    onSendClick = { chatViewModel.sendMessage() }
                )
            }
        }
    } else {
        OverlayLoadingScreen(scale = 1f)
    }
}

@Composable
private fun DisplayMessageSection(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    currentUser: User,
    interlocutor: User
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        reverseLayout = true,
    ) {
        if(messages.isNotEmpty()) {
            itemsIndexed(messages) { index, message ->
                val sameSender = index > 0 && message.senderId == messages[index - 1].senderId

                val spacerHeight: Dp = if (sameSender) {
                    MaterialTheme.spacing.extraSmall
                } else {
                    MaterialTheme.spacing.smallMedium
                }

                val isCurrentUserSender = message.senderId == currentUser.id

                Spacer(modifier = Modifier.height(spacerHeight))

                if (isCurrentUserSender) {
                    SentMessageItem(text = message.text)
                } else {
                    val displayProfilePicture: Boolean = index == 0 || !sameSender

                    val receiveMessageItemModifier = if (displayProfilePicture) {
                        Modifier
                    } else {
                        Modifier.padding(start = MaterialTheme.spacing.extraLarge)
                    }

                    ReceiveMessageItem(
                        modifier = receiveMessageItemModifier,
                        message = message,
                        displayProfilePicture = displayProfilePicture,
                        profilePictureUrl = interlocutor.profilePictureUrl
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageInputSection(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = MaterialTheme.spacing.small),
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = Modifier
                .clip(ShapeDefaults.ExtraLarge)
                .background(GedoiseColor.LightGray)
                .weight(1f)
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {
            TransparentFocusedTextField(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.smallMedium),
                onValueChange = onTextChange,
                placeholder = { Text(text = stringResource(id = R.string.message_placeholder)) },
                backgroundColor = GedoiseColor.LightGray,
                displayKeyboard = false
            )
        }

        if(text.isNotBlank()) {
            IconButton(
                onClick = onSendClick,
                colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    disabledContainerColor = IconButtonDefaults.iconButtonColors().disabledContainerColor,
                    disabledContentColor = IconButtonDefaults.iconButtonColors().disabledContentColor
                )
            ) {
                Icon(
                    modifier = Modifier.scale(0.8f),
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(id = R.string.send_message_icon_description)
                )
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
                    modifier = Modifier.padding(
                        top = innerPadding.calculateTopPadding(),
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.small
                    )
                ) {
                    DisplayMessageSection(
                        modifier = Modifier.weight(1f),
                        messages = messagesFixture,
                        currentUser = userFixture,
                        interlocutor = conversationFixture.interlocutor
                    )

                    Row(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(ShapeDefaults.ExtraLarge)
                                .background(GedoiseColor.LightGray)
                                .weight(1f)
                                .padding(horizontal = MaterialTheme.spacing.medium)
                        ) {
                            TransparentFocusedTextField(
                                modifier = Modifier.padding(vertical = MaterialTheme.spacing.smallMedium),
                                defaultValue = text,
                                onValueChange = { text = it },
                                placeholder = { Text(stringResource(id = R.string.message_placeholder)) },
                                backgroundColor = GedoiseColor.LightGray
                            )
                        }

                        if(text.isNotBlank()) {
                            IconButton(
                                onClick = { },
                                colors = IconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White,
                                    disabledContainerColor = IconButtonDefaults.iconButtonColors().disabledContainerColor,
                                    disabledContentColor = IconButtonDefaults.iconButtonColors().disabledContentColor
                                ),
                            ) {
                                Icon(
                                    modifier = Modifier.scale(0.8f),
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}