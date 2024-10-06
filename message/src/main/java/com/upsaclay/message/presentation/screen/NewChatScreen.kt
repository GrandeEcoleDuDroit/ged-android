package com.upsaclay.message.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.presentation.components.TransparentFocusedTextField
import com.upsaclay.common.presentation.theme.GedoiseColor
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.message.R
import com.upsaclay.message.domain.model.ChatState
import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.presentation.components.ChatTopBar
import com.upsaclay.message.presentation.components.ReceiveMessageItem
import com.upsaclay.message.presentation.components.SentMessageItem
import com.upsaclay.message.presentation.viewmodel.ChatViewModel

@Composable
fun NewChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel,
    interlocutorId: Int
) {
    LaunchedEffect(Unit) {
        chatViewModel.setInterlocutor(interlocutorId)
    }
    val interlocutor = chatViewModel.interlocutor.collectAsState().value
    val currentUser = chatViewModel.currentUser
    val text = chatViewModel.text
    val chatState = chatViewModel.chatState.collectAsState().value

    if(chatState == ChatState.LOADING) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
    }

    if (interlocutor != null && currentUser != null) {
        Scaffold(
            topBar = {
                ChatTopBar(
                    navController = navController,
                    interlocutor = interlocutor
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
                MessageSection(
                    modifier = Modifier.weight(1f),
                    messages = conversation?.messages ?: emptyList(),
                    currentUser = currentUser
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
                            onValueChange = { chatViewModel.updateText(it) },
                            placeholder = { Text(text = stringResource(id = R.string.message_placeholder)) },
                            backgroundColor = GedoiseColor.LightGray,
                            displayKeyboard = false
                        )
                    }

                    if(text.isNotBlank()) {
                        IconButton(
                            onClick = { chatViewModel.sendMessage() },
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
        if(messages.isNotEmpty()) {
            itemsIndexed(messages) { index, message ->
                val sameSender = index > 0 && message.sender == messages[index - 1].sender
                val spacing =
                    if (sameSender) MaterialTheme.spacing.extraSmall else MaterialTheme.spacing.smallMedium
                val isCurrentUserSender = message.sender.id == currentUser.id

                Spacer(modifier = Modifier.height(spacing))

                if (isCurrentUserSender) {
                    SentMessageItem(text = message.text)
                } else {
                    val displayProfilePicture = index == 0 || !sameSender
                    val paddingModifier =
                        if (displayProfilePicture) Modifier else Modifier.padding(start = MaterialTheme.spacing.extraLarge)
                    ReceiveMessageItem(
                        modifier = paddingModifier,
                        message = message,
                        displayProfilePicture = displayProfilePicture
                    )
                }
            }
        }
    }
}