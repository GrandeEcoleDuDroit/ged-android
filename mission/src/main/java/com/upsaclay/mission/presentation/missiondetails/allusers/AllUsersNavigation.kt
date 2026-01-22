package com.upsaclay.mission.presentation.missiondetails.allusers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.entity.User
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersRoute(val usersJson: String): Route

fun NavController.navigateToAllUsers(users: List<User>) {
    navigate(AllUsersRoute(Gson().toJson(users)))
}

fun NavGraphBuilder.allUsersScreen(
    onBackClick: () -> Unit,
    onUserClick: (User) -> Unit
) {
    composable<AllUsersRoute> { entry ->
        val type = object : TypeToken<List<User>>() {}.type
        val users = entry.toRoute<AllUsersRoute>().usersJson
            .let { Gson().fromJson<List<User>>(it, type) }

        AllUsersDestination(
            users = users,
            onBackClick = onBackClick,
            onUserClick = onUserClick
        )
    }
}
