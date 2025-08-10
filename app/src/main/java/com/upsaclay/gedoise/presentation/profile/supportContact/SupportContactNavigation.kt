package com.upsaclay.gedoise.presentation.profile.supportContact

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object SupportContactRoute : Route

fun NavController.navigateToSupportContact(): Unit {
    navigate(route = SupportContactRoute)
}

fun NavGraphBuilder.supportContactScreen(
    onBackClick: () -> Unit): Unit {
    composable<SupportContactRoute> {
        SupportContactDestination(onBackClick = onBackClick)
    }

}