package com.upsaclay.news.presentation.announcement

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

object AnnouncementPresentationUtils {
    const val MAX_TITLE_LENGTH = 200
    const val MAX_CONTENT_LENGTH = 2000

    val announcementTitleStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val announcementContentStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyLarge

    val extendedAnnouncementItemTitleStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleMedium

    val extendedAnnouncementItemContentStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyMedium
}