package com.upsaclay.forum.presentation.createmission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable data object CreateMissionRoute

fun NavController.navigateToCreateMission() {
    navigate(route = CreateMissionRoute)
}

fun NavGraphBuilder.createMissionScreen(
    onCreateMissionClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable<CreateMissionRoute> {
        CreateMissionDestination(
            onCreateMissionClick = onCreateMissionClick,
            onBackClick = onBackClick
        )
    }
}