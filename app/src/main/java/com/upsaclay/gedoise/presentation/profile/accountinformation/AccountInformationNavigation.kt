package com.upsaclay.gedoise.presentation.profile.accountinformation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object AccountInformationRoute: Route

fun NavController.navigateToAccountInformation() {
    navigate(route = AccountInformationRoute)
}

fun NavGraphBuilder.accountInformationScreen(
    onBackClick: () -> Unit
) {
    composable<AccountInformationRoute> {
        AccountInformationDestination(
            onBackClick = onBackClick
        )
    }
}