package com.upsaclay.gedoise.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.upsaclay.core.data.Screen
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.data.NavigationItem
import com.upsaclay.core.R as CoreResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    navController: NavController
){
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.school_name),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.navigate(Screen.HOME.route) }
            ) {
                Image(
                    painter = painterResource(id = com.upsaclay.core.R.drawable.ged_logo),
                    contentDescription = stringResource(id = com.upsaclay.core.R.string.ged_logo_description),
                    contentScale = ContentScale.Fit
                )
            }
        },
        actions = {
            IconButton(
                onClick = { navController.navigate(Screen.PROFILE.route) },
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    painter = painterResource(id = CoreResource.drawable.ic_person),
                    contentDescription = stringResource(id = R.string.profile_icon_description),
                    contentScale = ContentScale.Fit
                )
            }
        }
    )
}

@Composable
fun MainBottomBar(
    navController: NavController,
    navigationItems: List<NavigationItem>
){
    val currentRoute = remember {
        navController.currentDestination?.route
    }

    NavigationBar{
        navigationItems.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = navigationItem.screen.route == currentRoute,
                onClick = {
                    if(navigationItem.screen.route != currentRoute)
                        navController.navigate(navigationItem.screen.route)
                },
                icon = {
                    BadgedBox(badge = {
                        if (navigationItem.badges > 0) {
                            Badge {
                                Text(text = navigationItem.badges.toString())
                            }
                        } else if (navigationItem.hasNews) {
                            Badge()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = navigationItem.icon),
                            contentDescription = stringResource(id = navigationItem.iconDescription)
                        )
                    }
                },
                label = {
                    Text(text = stringResource(id = navigationItem.label))
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainTopBarPreview(){
    GedoiseTheme {
        MainTopBar(NavController(LocalContext.current))
    }
}

@Preview
@Composable
private fun MainBottomBarPreview(){
    val messageWithNotif = NavigationItem.Message()
    messageWithNotif.badges = 5

    val calendarWithNews = NavigationItem.Calendar()
    calendarWithNews.hasNews = true

    val itemList = listOf(
        NavigationItem.Home(),
        messageWithNotif,
        calendarWithNews,
        NavigationItem.Forum(),
    )

    GedoiseTheme {
        MainBottomBar(NavController(LocalContext.current), itemList)
    }
}
