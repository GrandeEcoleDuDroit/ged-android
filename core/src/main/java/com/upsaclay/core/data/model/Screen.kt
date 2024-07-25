package com.upsaclay.core.data.model

enum class Screen(val route: String) {
    AUTHENTICATION("authentication_screen"),
    FIRST_REGISTRATION_SCREEN("first_registration_screen"),
    SECOND_REGISTRATION_SCREEN("second_registration_screen"),
    THIRD_REGISTRATION_SCREEN("third_registration_screen"),
    HOME("home_screen"),
    MESSAGE("message_screen"),
    CALENDAR( "calendar_screen"),
    FORUM( "forum_screen"),
    PROFILE("profile_screen")
}