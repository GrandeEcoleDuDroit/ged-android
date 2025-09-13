package com.upsaclay.news.presentation.announcement.allannouncements

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.serialization.Serializable

@Serializable data object AllAnnouncementRoute: Route

fun NavController.navigateToAllAnnouncement() {
    navigate(route = AllAnnouncementRoute)
}

fun NavGraphBuilder.allAnnouncementScreen(
    onBackClick: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit
) {
    composable<AllAnnouncementRoute> {
        AllAnnouncementsDestination(
            onBackClick = onBackClick,
            onEditAnnouncementClick = onEditAnnouncementClick,
            onAnnouncementClick = onAnnouncementClick
        )
    }
}