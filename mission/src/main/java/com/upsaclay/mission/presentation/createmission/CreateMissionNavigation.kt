package com.upsaclay.mission.presentation.createmission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object CreateMissionRoute: Route

fun NavController.navigateToCreateMission() {
    navigate(route = CreateMissionRoute)
}

fun NavGraphBuilder.createMissionScreen(
    onBackClick: () -> Unit
) {
    composable<CreateMissionRoute> {
        CreateMissionDestination(
            onBackClick = onBackClick
        )
    }
}