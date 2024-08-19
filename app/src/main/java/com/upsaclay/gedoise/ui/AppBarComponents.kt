package com.upsaclay.gedoise.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.upsaclay.common.data.model.Screen
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.ui.components.ProfilePicture
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.data.BottomNavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    navController: NavController,
    user: User
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
                onClick = { navController.navigate(Screen.NEWS.route) }
            ) {
                Image(
                    painter = painterResource(id = com.upsaclay.common.R.drawable.ged_logo),
                    contentDescription = stringResource(id = com.upsaclay.common.R.string.ged_logo_description),
                    contentScale = ContentScale.Fit,
                )
            }
        },
        actions = {
            IconButton(
                onClick = { navController.navigate(Screen.PROFILE.route) },
                modifier = Modifier
                    .clip(shape = CircleShape)
            ) {
                ProfilePicture(imageUrl = user.profilePictureUrl)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackSmallTopBar(
    navController: NavController,
    title: String,
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = com.upsaclay.common.R.string.arrow_back_icon_description)
        )
    }
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                icon()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopAppBar(
    title: String,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
        actions = {
            TextButton(onClick = onSaveClick) {
                Text(
                    text = stringResource(id = com.upsaclay.common.R.string.save),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun MainBottomBar(
    navController: NavController,
    bottomNavigationItems: List<BottomNavigationItem>
){
    val currentRoute = remember {
        navController.currentDestination?.route
    }

    NavigationBar{
        bottomNavigationItems.forEachIndexed { _, navigationItem ->
            NavigationBarItem(
                selected = navigationItem.screen.route == currentRoute,
                onClick = {
                    if(navigationItem.screen.route != currentRoute)
                        navController.navigate(navigationItem.screen.route)
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (navigationItem.badges > 0) {
                                Badge { Text(text = navigationItem.badges.toString()) }
                            }
                            else if (navigationItem.hasNews) {
                                Badge()
                            }
                        }
                    ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun MainTopBarPreview(){
    GedoiseTheme {
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
                    onClick = { }
                ) {
                    Image(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.ged_logo),
                        contentDescription = stringResource(id = com.upsaclay.common.R.string.ged_logo_description),
                        contentScale = ContentScale.Fit
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .clip(shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.default_profile_picture),
                        contentDescription = stringResource(id = R.string.profile_icon_description),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.border(1.dp, Color.LightGray, CircleShape)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun SmallTopBarPreview(){
    GedoiseTheme {
        BackSmallTopBar(
            navController = NavController(LocalContext.current),
            title = "Title",
        )
    }
}

@Preview
@Composable
private fun EditTopAppBarPreview() {
    GedoiseTheme {
        EditTopAppBar(
            title = "Edit",
            onCancelClick = { },
            onSaveClick = { }
        )
    }
}

@Preview
@Composable
private fun MainBottomBarPreview(){
    val messageWithNotif = BottomNavigationItem.Message()
    messageWithNotif.badges = 5

    val calendarWithNews = BottomNavigationItem.Calendar()
    calendarWithNews.hasNews = true

    val itemList = listOf(
        BottomNavigationItem.Home(),
        messageWithNotif,
        calendarWithNews,
        BottomNavigationItem.Forum(),
    )

    GedoiseTheme {
        MainBottomBar(NavController(LocalContext.current), itemList)
    }
}
