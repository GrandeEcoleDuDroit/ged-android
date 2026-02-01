package com.upsaclay.authentication.presentation.forgottenpassword

import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.NavGraphBuilder
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object ForgottenPasswordRoute : Route


fun NavController.navigateToForgottenPassword() {
    navigate(route = ForgottenPasswordRoute)
}
fun NavGraphBuilder.forgottenPasswordScreen(
    onBackClick: () -> Unit,
) {
    composable<ForgottenPasswordRoute>{
        ForgottenPasswordDestination(
            onBackClick = onBackClick
        )
    }
}