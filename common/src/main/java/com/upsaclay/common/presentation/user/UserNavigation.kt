package com.upsaclay.common.presentation.user

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.upsaclay.common.domain.UserJsonParser
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.entity.User
import kotlinx.serialization.Serializable

@Serializable data class UserRoute(val userJson: String): Route {
    companion object {
        fun plainTextRoute(): String {
            return "${UserRoute::class.simpleName}/{userJson}"
        }

        fun route(user: User): String {
            val encodedJson = Uri.encode(UserJsonParser.toJson(user))
            return "${UserRoute::class.simpleName}/$encodedJson"
        }
    }
}

fun NavController.navigateToUser(user: User) {
    navigate(route = UserRoute.route(user))
}

fun NavGraphBuilder.userScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = UserRoute.plainTextRoute(),
        arguments = listOf(navArgument("userJson") { type = NavType.StringType })
    ) { backStackEntry ->
        val user = backStackEntry.arguments?.getString("userJson")?.let {
            val decoded = Uri.decode(it)
            UserJsonParser.fromJson(decoded)
        }

        user?.let {
            UserDestination(
                onBackClick = onBackClick,
                user = it
            )
        } ?: onBackClick
    }
}