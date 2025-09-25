package com.upsaclay.gedoise.presentation.profile.privacy

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object PrivacyRoute: Route

fun NavController.navigateToPrivacy() {
    navigate(route = PrivacyRoute)
}

fun NavGraphBuilder.privacyScreen(
    onBackClick: () -> Unit,
    onBlockedUsersClick: () -> Unit
) {
    composable<PrivacyRoute> {
        PrivacyDestination(
            onBackClick = onBackClick,
            onBlockedUsersClick = onBlockedUsersClick
        )
    }
}