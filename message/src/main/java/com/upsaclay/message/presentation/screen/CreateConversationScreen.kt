package com.upsaclay.message.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.gson.Gson
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.userFixtures
import com.upsaclay.message.presentation.components.UserItem
import com.upsaclay.message.presentation.viewmodel.ConversationViewModel

@Composable
fun CreateConversationScreen(
    navController: NavController,
    conversationViewModel: ConversationViewModel
) {
    val users = conversationViewModel.users.collectAsState(emptyList()).value
    val gson = Gson()

    LazyColumn {
        items(users) { user ->
            UserItem(user = user, onClick = {
                val userJson = gson.toJson(user)
                navController.navigate(Screen.CHAT.route + "?user=$userJson")
            })
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
private fun CreateConversationScreenPreview() {
    GedoiseTheme {
        LazyColumn {
            items(userFixtures) { user ->
                UserItem(user = user, onClick = { })
            }
        }
    }
}