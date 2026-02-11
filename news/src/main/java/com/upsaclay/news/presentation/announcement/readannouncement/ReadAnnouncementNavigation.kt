package com.upsaclay.news.presentation.announcement.readannouncement

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.entity.User
import com.upsaclay.news.domain.announcement.Announcement
import kotlinx.serialization.Serializable

@Serializable data class ReadAnnouncementRoute(val announcementId: String): Route

fun NavController.navigateToReadAnnouncement(announcementId: String) {
    navigate(route = ReadAnnouncementRoute(announcementId))
}

fun NavGraphBuilder.readAnnouncementScreen(
    onBackClick: () -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onAuthorClick: (User) -> Unit
) {
    composable<ReadAnnouncementRoute> {
        val announcementId = it.toRoute<ReadAnnouncementRoute>().announcementId
        ReadAnnouncementDestination(
            announcementId = announcementId,
            onBackClick = onBackClick,
            onEditAnnouncementClick = onEditAnnouncementClick,
            onAuthorClick = onAuthorClick
        )
    }
}