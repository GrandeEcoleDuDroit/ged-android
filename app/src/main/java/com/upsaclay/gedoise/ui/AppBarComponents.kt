package com.upsaclay.gedoise.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.upsaclay.core.data.Screen
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.gedoise.R
import com.upsaclay.core.R as CoreResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.school_name)) },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(Screen.HOME.route)
                }) {
                Image(
                    painter = painterResource(id = CoreResource.drawable.ged_logo),
                    contentDescription = stringResource(id = CoreResource.string.ged_logo_description),
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

@Preview
@Composable
private fun MainTopAppBarPreview(){
    GedoiseTheme {
        MainTopAppBar(NavController(LocalContext.current))
    }
}