package com.upsaclay.core.ui

sealed class Screen(val route: String) {
    data object Authentication : Screen("authentication_screen")
    data object Home : Screen("home_screen")
}