package com.upsaclay.news.presentation.post.readpost

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.news.domain.post.Post
import kotlinx.serialization.Serializable

@Serializable data class ReadPostRoute(val postId: String): Route

fun NavController.navigateToReadPost(postId: String) {
    navigate(route = ReadPostRoute(postId))
}

fun NavGraphBuilder.readPostScreen(
    onBackClick: () -> Unit,
    onEditPostClick: (Post) -> Unit
) {
    composable<ReadPostRoute> {
        val postId = it.toRoute<ReadPostRoute>().postId
        ReadPostDestination(
            postId = postId,
            onBackClick = onBackClick,
            onEditPostClick = onEditPostClick
        )
    }
}