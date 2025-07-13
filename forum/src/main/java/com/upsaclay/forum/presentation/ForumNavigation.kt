package com.upsaclay.forum.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.forum.domain.entity.Mission
import kotlinx.serialization.Serializable

@Serializable data object ForumBaseRoute: Route
@Serializable data object ForumRoute: Route

fun NavController.navigateToForum(navOptions: NavOptions? = null) {
    navigate(route = ForumBaseRoute, navOptions = navOptions)
}

fun NavGraphBuilder.forumSection(
    bottomBar: @Composable () -> Unit,
    onMissionClick: (Mission) -> Unit,
    forumDestinations: NavGraphBuilder.() -> Unit
) {
    navigation<ForumBaseRoute>(startDestination = ForumRoute) {
        composable<ForumRoute> {
            ForumDestination(
                bottomBar = bottomBar,
                onMissionClick = onMissionClick
            )
        }
        forumDestinations()
    }
}