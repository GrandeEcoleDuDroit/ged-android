package com.upsaclay.news.presentation.announcement.allannouncements

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.entity.User
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.serialization.Serializable

@Serializable data object AllAnnouncementsRoute: Route

fun NavController.navigateToAllAnnouncements() {
    navigate(route = AllAnnouncementsRoute)
}

fun NavGraphBuilder.allAnnouncementsScreen(
    onBackClick: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onAuthorClick: (User) -> Unit
) {
    composable<AllAnnouncementsRoute> {
        AllAnnouncementsDestination(
            onBackClick = onBackClick,
            onEditAnnouncementClick = onEditAnnouncementClick,
            onAnnouncementClick = onAnnouncementClick,
            onAuthorClick = onAuthorClick
        )
    }
}