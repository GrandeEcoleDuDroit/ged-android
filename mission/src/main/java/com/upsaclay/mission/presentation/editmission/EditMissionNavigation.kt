package com.upsaclay.mission.presentation.editmission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.mission.domain.MissionJsonParser
import com.upsaclay.mission.domain.entity.Mission
import kotlinx.serialization.Serializable

@Serializable data class EditMissionRoute(val missionJson: String): Route

fun NavController.navigateToEditMission(mission: Mission) {
    navigate(EditMissionRoute(MissionJsonParser.toJson(mission)))
}

fun NavGraphBuilder.editMissionScreen(
    onBackClick: () -> Unit
) {
    composable<EditMissionRoute> { entry ->
        val mission = entry.toRoute<EditMissionRoute>().missionJson
            .let(MissionJsonParser::toMission)

        EditMissionDestination(
            mission = mission,
            onBackClick = onBackClick
        )
    }
}
