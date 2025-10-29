package com.upsaclay.authentication.presentation.registration.thirdregistration

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.SchoolLevel
import kotlinx.serialization.Serializable

@Serializable
data class ThirdRegistrationRoute(
    val firstName: String,
    val lastName: String,
    val schoolLevel: SchoolLevel
)


fun NavController.navigateToThirdRegistration(
    firstName: String,
    lastName: String,
    schoolLevel: SchoolLevel
) {
    navigate(route = ThirdRegistrationRoute(firstName, lastName, schoolLevel))
}

fun NavGraphBuilder.thirdRegistrationScreen(
    onBackClick: () -> Unit,
    onRegistrationClick: () -> Unit
) {
    composable<ThirdRegistrationRoute> { entry ->
        val firstName = entry.toRoute<ThirdRegistrationRoute>().firstName
        val lastName = entry.toRoute<ThirdRegistrationRoute>().lastName
        val schoolLevel = entry.toRoute<ThirdRegistrationRoute>().schoolLevel

        ThirdRegistrationDestination(
            firstName = firstName,
            lastName = lastName,
            schoolLevel = schoolLevel,
            onBackClick = onBackClick,
            onRegistrationClick = onRegistrationClick
        )
    }
}