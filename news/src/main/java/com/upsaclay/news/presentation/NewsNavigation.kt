package com.upsaclay.news.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.presentation.news.NewsDestination
import kotlinx.serialization.Serializable

@Serializable data object NewsBaseRoute: Route
@Serializable data object NewsRoute: Route

fun NavController.navigateToNews(navOptions: NavOptions? = null) {
    navigate(route = NewsBaseRoute, navOptions = navOptions)
}

fun NavGraphBuilder.newsSection(
    onAnnouncementClick: (String) -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onSeeAllAnnouncementsClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    newsDestinations: NavGraphBuilder.() -> Unit
) {
    navigation<NewsBaseRoute>(startDestination = NewsRoute) {
        composable<NewsRoute> {
            NewsDestination(
                onAnnouncementClick = onAnnouncementClick,
                onCreateAnnouncementClick = onCreateAnnouncementClick,
                onEditAnnouncementClick = onEditAnnouncementClick,
                onSeeAllAnnouncementsClick = onSeeAllAnnouncementsClick,
                onCreatePostClick = onCreatePostClick,
                bottomBar = bottomBar
            )
        }
        newsDestinations()
    }
}