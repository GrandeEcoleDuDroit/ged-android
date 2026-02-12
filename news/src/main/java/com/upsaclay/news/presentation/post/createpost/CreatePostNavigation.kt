package com.upsaclay.news.presentation.post.createpost

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable
data object CreatePostRoute: Route

fun NavController.navigateToCreatePost() {
    navigate(route = CreatePostRoute)
}

fun NavGraphBuilder.createPostScreen(
    onBackClick: () -> Unit
) {
    composable<CreatePostRoute> {
        CreatePostDestination(
            onCancelClick = onBackClick
        )
    }
}