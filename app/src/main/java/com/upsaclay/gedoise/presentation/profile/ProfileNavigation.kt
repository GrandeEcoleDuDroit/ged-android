package com.upsaclay.gedoise.presentation.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object ProfileBaseRoute: Route
@Serializable data object ProfileRoute: Route

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(route = ProfileBaseRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profileSection(
    onAccountInformationClick: () -> Unit,
    onAccountClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    profileDestinations: NavGraphBuilder.() -> Unit
) {
    navigation<ProfileBaseRoute>(startDestination = ProfileRoute) {
        composable<ProfileRoute> {
            ProfileDestination(
                onAccountInformationClick = onAccountInformationClick,
                onAccountClick = onAccountClick,
                onPrivacyClick = onPrivacyClick,
                bottomBar = bottomBar
            )
        }
        profileDestinations()
    }
}