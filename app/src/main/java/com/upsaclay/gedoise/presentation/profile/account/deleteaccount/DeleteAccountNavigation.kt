package com.upsaclay.gedoise.presentation.profile.account.deleteaccount

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object DeleteAccountRoute: Route

fun NavController.navigateToDeleteAccount() {
    navigate(route = DeleteAccountRoute)
}

fun NavGraphBuilder.deleteAccountScreen(
    onBackClick: () -> Unit
) {
    composable<DeleteAccountRoute> {
        DeleteAccountDestination(
            onBackClick = onBackClick
        )
    }
}