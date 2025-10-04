package com.upsaclay.gedoise.presentation.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.gedoise.presentation.navigation.TopLevelDestination
import com.upsaclay.gedoise.presentation.navigation.TopLevelDestinationRoute

@Composable
fun MainBottomBar(
    onTopLevelDestinationClick: (TopLevelDestinationRoute) -> Unit,
    currentRoute: NavDestination?,
    topLevelDestinations: List<TopLevelDestination>
) {
    var previousDestination: TopLevelDestination? = null

    NavigationBar(
        tonalElevation = 2.dp
    ) {
        topLevelDestinations.forEachIndexed { index, destination ->
            val selected = currentRoute
                .isRouteInHierarchy(destination.route)
                .also {
                    if (it) {
                        previousDestination = destination
                    }
                }

            val iconRes = if (selected) destination.filledIcon else destination.outlinedIcon

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (destination.route != previousDestination?.route) {
                        onTopLevelDestinationClick(TopLevelDestinationRoute.entries[index])
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (destination.badges > 0) {
                                Badge { Text(text = destination.badges.toString()) }
                            } else if (destination.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = stringResource(id = destination.iconDescription)
                        )
                    }
                },
                label = { Text(text = stringResource(id = destination.label)) }
            )
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: Route) =
    this?.parent?.any {
        it.hasRoute(route::class)
    } ?: false

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun MainBottomBarPreview() {
    val navController = rememberNavController()

    val itemList = listOf(
        TopLevelDestination.Home,
        TopLevelDestination.Message(badges = 5)
    )

    GedoiseTheme {
        Surface {
            MainBottomBar(
                onTopLevelDestinationClick = { },
                currentRoute = navController.currentBackStackEntryAsState().value?.destination,
                topLevelDestinations = itemList
            )
        }
    }
}