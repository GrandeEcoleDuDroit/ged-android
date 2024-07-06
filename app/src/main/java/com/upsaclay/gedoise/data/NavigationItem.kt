package com.upsaclay.gedoise.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.upsaclay.core.data.Screen
import com.upsaclay.gedoise.R
import com.upsaclay.core.R as CoreResource

sealed class NavigationItem(
    val screen: Screen,
    val label: String,
    @DrawableRes val icon: Int,
    @StringRes val iconDescription: Int
) {
    data object Home: NavigationItem(
        Screen.HOME,
        "Accueil",
        CoreResource.drawable.ic_home,
        R.string.home_icon_description
    )
    data object Message: NavigationItem(
        Screen.MESSAGE,
        "Messagerie",
        CoreResource.drawable.ic_mail_outline,
        R.string.mail_icon_description
    )
    data object Calendar: NavigationItem(
        Screen.CALENDAR,
        "Calendrier",
        CoreResource.drawable.ic_calendar,
        R.string.calendar_icon_description
    )
    data object Forum: NavigationItem(
        Screen.FORUM,
        "Forum",
        CoreResource.drawable.ic_forum,
        R.string.forum_icon_description
    )
}