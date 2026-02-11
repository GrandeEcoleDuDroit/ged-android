package com.upsaclay.news.presentation.post

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

object PostPresentationUtils {
    const val MAX_TITLE_LENGTH = 100
    const val MAX_POST_LINK_LENGTH = 2048
    const val MAX_CONTENT_LENGTH = 3000
    const val MAX_IMAGE_COUNT = 10

    val titleStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val postLinkStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyMedium

    val contentStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyLarge
}