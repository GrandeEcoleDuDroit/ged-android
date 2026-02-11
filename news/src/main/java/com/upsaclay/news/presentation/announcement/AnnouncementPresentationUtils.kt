package com.upsaclay.news.presentation.announcement

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

object AnnouncementPresentationUtils {
    const val MAX_TITLE_LENGTH = 200
    const val MAX_CONTENT_LENGTH = 2000

    val titleStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val contentStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyLarge
}