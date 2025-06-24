package com.upsaclay.authentication.presentation.forgopassword

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable
data object ForgotPasswordBaseRoute: Route
@Serializable
data object ForgotPasswordRoute: Route

fun NavGraphBuilder.forgotPasswordSection(
    onBackClick: () -> Unit,
) {
    navigation<ForgotPasswordBaseRoute>(startDestination = ForgotPasswordRoute) {
        composable<ForgotPasswordRoute> {
            ForgotPasswordDestination(
                onBackClick = onBackClick
            )
        }
    }
}