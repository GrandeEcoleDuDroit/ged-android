package com.upsaclay.gedoise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.gedoise.data.BottomNavigationItem
import com.upsaclay.news.ui.NewsScreenPreview

@Preview
@Composable
private fun NewsScreenWithNavbarPreview(){
    val messageWithNotif = BottomNavigationItem.Message()
    messageWithNotif.badges = 5

    val calendarWithNews = BottomNavigationItem.Calendar()
    calendarWithNews.hasNews = true

    val navbarItemList = listOf(
        BottomNavigationItem.Home(),
        messageWithNotif,
        calendarWithNews,
        BottomNavigationItem.Forum(),
    )

    GedoiseTheme {
        Scaffold(
            topBar = {
                MainTopBarPreview()
            },
            bottomBar = {
                MainBottomBar (
                    navController = NavController(LocalContext.current),
                    navbarItemList
                )
            }
        ) {
            Box(
                modifier = Modifier.padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                )
            ) {
                NewsScreenPreview()
            }
        }
    }
}