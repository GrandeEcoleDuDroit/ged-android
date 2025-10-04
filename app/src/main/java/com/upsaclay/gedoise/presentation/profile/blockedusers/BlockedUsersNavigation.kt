package com.upsaclay.gedoise.presentation.profile.blockedusers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.entity.User
import kotlinx.serialization.Serializable

@Serializable data object BlockedUsersRoute: Route

fun NavController.navigateToBlockedUsers() {
    navigate(route = BlockedUsersRoute)
}

fun NavGraphBuilder.blockedUsersScreen(
    onBackClick: () -> Unit,
    onAccountClick: (User) -> Unit
) {
    composable<BlockedUsersRoute> {
        BlockedUsersDestination(
            onBackClick = onBackClick,
            onAccountClick = onAccountClick
        )
    }
}