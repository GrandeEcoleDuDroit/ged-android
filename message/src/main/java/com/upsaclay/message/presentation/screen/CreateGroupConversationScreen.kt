package com.upsaclay.message.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.gson.Gson
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.message.presentation.components.UserItem
import com.upsaclay.message.presentation.viewmodel.ConversationViewModel

@Composable
fun CreateGroupConversationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    conversationViewModel: ConversationViewModel
) {
    val users = conversationViewModel.users.collectAsState(emptyList()).value
    val gson = Gson()

    LazyColumn(modifier = modifier) {
        items(users) { user ->
            UserItem(
                user = user,
                onClick = {
                    val userJson = gson.toJson(user)
                    navController.navigate(Screen.CHAT.route + "?user=$userJson")
                }
            )
        }
    }
}