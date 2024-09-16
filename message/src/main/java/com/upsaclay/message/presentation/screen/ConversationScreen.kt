package com.upsaclay.message.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.message.R
import com.upsaclay.message.presentation.components.ConversationItem
import com.upsaclay.message.presentation.viewmodel.ConversationViewModel
import com.upsaclay.message.utils.conversationsItemsDataFixture
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConversationScreen(
    navController: NavController,
    conversationViewModel : ConversationViewModel = koinViewModel()
) {
    val conversations = conversationViewModel.conversations.collectAsState(emptyList()).value

    Box {
        LazyColumn {
            items(conversations) { conversation ->
                ConversationItem(
                    modifier = Modifier.fillMaxWidth(),
                    conversationItemData = conversation,
                    onClick = {
                        navController.navigate(Screen.CHAT.route + "?conversationId=${conversation.id}")
                    },
                    onLongClick = { }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        ) {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
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

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun ConversationScreenPreview() {
    GedoiseTheme {
        LazyColumn {
            items(conversationsItemsDataFixture) { conversation ->
                ConversationItem(
                    modifier = Modifier.fillMaxWidth(),
                    conversationItemData = conversation,
                    onClick = { },
                    onLongClick = { }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        ) {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
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