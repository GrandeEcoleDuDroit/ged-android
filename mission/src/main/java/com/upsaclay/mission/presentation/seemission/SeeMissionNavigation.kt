package com.upsaclay.mission.presentation.seemission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.entity.User
import kotlinx.serialization.Serializable

@Serializable data class SeeMissionRoute(val missionId: Int): Route

fun NavController.navigateToSeeMission(missionId: Int) {
    navigate(route = SeeMissionRoute(missionId))
}

fun NavGraphBuilder.seeMissionScreen(
    onBackClick: () -> Unit,
    onManagerClick: (User) -> Unit
) {
    composable<SeeMissionRoute> {
        val missionId = it.toRoute<SeeMissionRoute>().missionId
        SeeMissionDestination(
            missionId = missionId,
            onBackClick = onBackClick,
            onManagerClick = onManagerClick
        )
    }
}