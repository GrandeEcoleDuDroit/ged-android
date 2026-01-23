package com.upsaclay.news.presentation.announcement

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

object AnnouncementPresentationUtils {
    val titleStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val contentStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyLarge
}