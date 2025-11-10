package com.upsaclay.mission.presentation.missiondetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.domain.entity.Mission
import kotlinx.serialization.Serializable

@Serializable data class MissionDetailsRoute(val missionId: String): Route

fun NavController.navigateToMissionDetails(missionId: String) {
    navigate(route = MissionDetailsRoute(missionId))
}

fun NavGraphBuilder.missionDetailsScreen(
    onBackClick: () -> Unit,
    onManagerClick: (User) -> Unit,
    onParticipantClick: (User) -> Unit,
    onEditMissionClick: (Mission) -> Unit
) {
    composable<MissionDetailsRoute> {
        val missionId = it.toRoute<MissionDetailsRoute>().missionId
        MissionDetailsDestination(
            missionId = missionId,
            onBackClick = onBackClick,
            onManagerClick = onManagerClick,
            onParticipantClick = onParticipantClick,
            onEditMissionClick = onEditMissionClick
        )
    }
}