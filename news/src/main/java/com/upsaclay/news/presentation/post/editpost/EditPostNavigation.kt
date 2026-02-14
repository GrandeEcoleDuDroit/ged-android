package com.upsaclay.news.presentation.post.editpost

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.presentation.post.PostGsonParser
import kotlinx.serialization.Serializable

@Serializable
data class EditPostRoute(val postJson: String): Route

fun NavController.navigateToEditPost(post: Post) {
    navigate(route = EditPostRoute(PostGsonParser.toJson(post)))
}

fun NavGraphBuilder.editPostScreen(
    onBackClick: () -> Unit
) {
    composable<EditPostRoute> { entry ->
        val post = entry.toRoute<EditPostRoute>().postJson
            .let(PostGsonParser::toPost)

        EditPostDestination(
            post = post,
            onCancelClick = onBackClick
        )
    }
}