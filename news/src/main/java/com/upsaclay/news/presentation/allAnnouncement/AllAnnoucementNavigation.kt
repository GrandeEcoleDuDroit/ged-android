package com.upsaclay.news.presentation.allAnnouncement

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object AllAnnouncementBaseRoute: Route
@Serializable data object AllAnnouncementRoute: Route

fun NavController.navigateToAllAnnouncement(navOptions: NavOptions? = null) {
    navigate(route = AllAnnouncementBaseRoute, navOptions = navOptions)
}

fun NavGraphBuilder.allAnnouncementScreen(
    onAnnouncementClick: (String) -> Unit,
    onBackClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    navigation<AllAnnouncementBaseRoute>(startDestination = AllAnnouncementRoute) {
        composable<AllAnnouncementRoute> {
            AllAnnouncementDestination(
                onAnnouncementClick = onAnnouncementClick,
                onBackClick = onBackClick,
                bottomBar = bottomBar
            )
        }
    }
}