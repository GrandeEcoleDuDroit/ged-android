package com.upsaclay.core.data

sealed class Screen(val route: String) {
    data object Authentication : Screen("authentication_screen")
    data object Home : Screen("home_screen")
}