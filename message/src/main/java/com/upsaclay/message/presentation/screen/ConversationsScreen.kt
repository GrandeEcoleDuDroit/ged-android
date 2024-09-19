package com.upsaclay.message.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.message.R
import com.upsaclay.message.presentation.components.ConversationItem
import com.upsaclay.message.presentation.viewmodel.ConversationViewModel
import com.upsaclay.message.utils.conversationsPreviewFixture
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConversationScreen(
    navController: NavController,
    conversationViewModel : ConversationViewModel = koinViewModel()
) {
    val conversations = conversationViewModel.conversations.collectAsState(emptyList()).value

    Box(modifier = Modifier.fillMaxSize()) {
        if (conversations.isEmpty()) {
            FlowRow(modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.no_conversation),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                TextButton(
                    contentPadding = PaddingValues(MaterialTheme.spacing.default),
                    modifier = Modifier.height(MaterialTheme.spacing.large),
                    onClick = {},
                ) {
                    Text(
                        text = stringResource(id = R.string.new_conversation),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        } else {
            LazyColumn {
                items(conversations) { conversation ->
                    ConversationItem(
                        modifier = Modifier.fillMaxWidth(),
                        conversationPreview = conversation,
                        onClick = {
                            navController.navigate(Screen.CHAT.route + "?conversationId=${conversation.id}")
                        },
                        onLongClick = { }
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(MaterialTheme.spacing.medium),
            onClick = { navController.navigate(Screen.CREATE_CONVERSATION.route) },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_message_add),
                contentDescription = stringResource(id = R.string.ic_message_add_description)
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
private fun ConversationsScreenPreview() {
    val conversations = conversationsPreviewFixture
//    val conversations = emptyList<ConversationPreview>()

    GedoiseTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (conversations.isEmpty()) {
                FlowRow(modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = stringResource(id = R.string.no_conversation),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                    TextButton(
                        contentPadding = PaddingValues(MaterialTheme.spacing.default),
                        modifier = Modifier.height(MaterialTheme.spacing.large),
                        onClick = {},
                    ) {
                        Text(
                            text = stringResource(id = R.string.new_conversation),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                ) {
                    items(conversations) { conversation ->
                        ConversationItem(
                            modifier = Modifier.fillMaxWidth(),
                            conversationPreview = conversation,
                            onClick = { },
                            onLongClick = { }
                        )
                    }
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(MaterialTheme.spacing.medium),
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_message_add),
                    contentDescription = stringResource(id = R.string.ic_message_add_description)
                )
            }
        }
    }
}