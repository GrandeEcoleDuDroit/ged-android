package com.upsaclay.news.presentation.post

import android.content.Intent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.core.net.toUri

object PostPresentationUtils {
    const val MAX_TITLE_LENGTH = 100
    const val MAX_POST_LINK_LENGTH = 2048
    const val MAX_CONTENT_LENGTH = 3000
    const val MAX_IMAGE_COUNT = 10

    val postTitleStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val postLinkStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyMedium

    val postContentStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyLarge

    fun getPostLinkIntent(postLink: String): Intent =
        Intent(Intent.ACTION_VIEW, postLink.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
}