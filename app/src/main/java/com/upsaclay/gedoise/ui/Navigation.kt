package com.upsaclay.gedoise.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.ui.AuthenticationScreen
import com.upsaclay.core.data.Screen
import com.upsaclay.gedoise.data.NavigationItem
import com.upsaclay.news.ui.NewsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.AUTHENTICATION.route
    ) {
        composable(Screen.AUTHENTICATION.route) {
            AuthenticationScreen(navController = navController)
        }
        composable(Screen.HOME.route) {
            MainStructure(navController = navController) {
                NewsScreen()
            }
        }
    }
}

@Composable
private fun MainStructure(
    navController: NavController,
    content: @Composable () -> Unit
){
    val itemList = listOf(
        NavigationItem.Home,
        NavigationItem.Message,
        NavigationItem.Calendar,
        NavigationItem.Forum,
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Scaffold(
            topBar = { MainTopAppBar(navController = navController) },
            bottomBar = {
                BottomNavigation(
                navController = navController,
                items = itemList
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                content()
            }
        }
    }
}