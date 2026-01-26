package com.upsaclay.authentication.forgottenpassword

import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.NavGraphBuilder
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object ForgottenPasswordNavigationRoute : Route


fun NavController.navigateToForgottenPasswordScreen() {
    navigate(route = ForgottenPasswordNavigationRoute)
}
fun NavGraphBuilder.forgottenPasswordScreen(
    onBackClick: () -> Unit,
) {
    composable<ForgottenPasswordNavigationRoute>{
        ForgottenPasswordDestination(
            onBackClick = onBackClick
        )
    }
}