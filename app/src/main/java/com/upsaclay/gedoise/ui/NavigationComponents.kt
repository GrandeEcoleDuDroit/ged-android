package com.upsaclay.gedoise.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.gedoise.data.NavigationItem

@Composable
fun BottomNavigation(
    navController: NavController,
    items: List<NavigationItem>
){
    NavigationBar{
        items.forEach { item ->
            AddItem(navController = navController, navigationItem = item)
        }
    }
}

@Composable
private fun RowScope.AddItem(
    navController: NavController,
    navigationItem: NavigationItem
){
    val currentRoute = remember {
        navController.currentDestination?.route
    }

    NavigationBarItem(
        modifier = Modifier.weight(1f),
        label = { Text(text = stringResource(id = navigationItem.label)) },
        icon = {
            Icon(
                painter = painterResource(id = navigationItem.icon),
                contentDescription = stringResource(id = navigationItem.iconDescription)
            )
        },
        selected = navigationItem.screen.route == currentRoute,
        alwaysShowLabel = true,
        onClick = {
            if(navigationItem.screen.route != currentRoute)
                navController.navigate(navigationItem.screen.route)
        },
    )
}

@Preview
@Composable
private fun BottomNavigationPreview(){
    val itemList = listOf(
        NavigationItem.Home(),
        NavigationItem.Message(),
        NavigationItem.Calendar(),
        NavigationItem.Forum(),
    )
    GedoiseTheme {
        BottomNavigation(
            navController = NavController(LocalContext.current),
            items = itemList
        )
    }
}