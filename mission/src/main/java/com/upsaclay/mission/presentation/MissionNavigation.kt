package com.upsaclay.mission.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.mission.domain.entity.Mission
import kotlinx.serialization.Serializable

@Serializable data object MissionBaseRoute: Route
@Serializable data object MissionRoute: Route

fun NavController.navigateToMission(navOptions: NavOptions? = null) {
    navigate(route = MissionBaseRoute, navOptions = navOptions)
}

fun NavGraphBuilder.missionSection(
    onMissionClick: (String) -> Unit,
    onCreateMissionClick: () -> Unit,
    onEditMissionClick: (Mission) -> Unit,
    bottomBar: @Composable () -> Unit,
    missionDestinations: NavGraphBuilder.() -> Unit
) {
    navigation<MissionBaseRoute>(startDestination = MissionRoute) {
        composable<MissionRoute> {
            MissionDestination(
                onMissionClick = onMissionClick,
                onCreateMissionClick = onCreateMissionClick,
                onEditMissionClick = onEditMissionClick,
                bottomBar = bottomBar
            )
        }
        missionDestinations()
    }
}