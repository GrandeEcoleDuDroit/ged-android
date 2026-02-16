package com.upsaclay.news.presentation.post.allposts

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.news.domain.post.Post
import kotlinx.serialization.Serializable

@Serializable data object AllPostsRoute: Route

fun NavController.navigateToAllPosts() {
    navigate(route = AllPostsRoute)
}

fun NavGraphBuilder.allPostsScreen(
    onBackClick: () -> Unit,
    onEditPostClick: (Post) -> Unit
) {
    composable<AllPostsRoute> {
        AllPostsDestination(
            onBackClick = onBackClick,
            onEditPostClick = onEditPostClick
        )
    }
}