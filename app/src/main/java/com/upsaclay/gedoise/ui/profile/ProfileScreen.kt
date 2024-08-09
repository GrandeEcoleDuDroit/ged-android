package com.upsaclay.gedoise.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.upsaclay.common.ui.components.CircularProgressBar
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.common.utils.userFixture
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.data.ProfileItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val user = profileViewModel.user.collectAsState(initial = null)

    val profileItems: ImmutableList<ProfileItem> = persistentListOf(
        ProfileItem(
            label = stringResource(id = R.string.account),
            iconRes = com.upsaclay.common.R.drawable.ic_person,
            iconDescription = stringResource(id = R.string.profile_icon_description),
            action = { }
        ),
        ProfileItem(
            label = stringResource(id = R.string.settings),
            iconRes = com.upsaclay.common.R.drawable.ic_settings,
            iconDescription = stringResource(id = R.string.settings_icon_description),
            action = { }
        ),
        ProfileItem(
            label = stringResource(id = R.string.support),
            iconRes = com.upsaclay.common.R.drawable.ic_support,
            iconDescription = stringResource(id = R.string.support_icon_description),
            action = { }
        ),
        ProfileItem(
            label = stringResource(id = R.string.logout),
            iconRes = com.upsaclay.common.R.drawable.ic_logout,
            iconDescription = stringResource(id = R.string.logout_icon_description),
            color = MaterialTheme.colorScheme.error,
            action = { profileViewModel.logout() }
        )
    )

    Scaffold(
        topBar = { ProfileTopBar(navController = navController) }
    ) {
        Box(Modifier.padding(top = it.calculateTopPadding())) {
            Column {
                user.value?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(MaterialTheme.spacing.medium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = user.profilePictureUrl,
                            contentDescription = stringResource(id = R.string.profile_icon_description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(150.dp)
                                .border(1.dp, Color.LightGray, CircleShape)
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                        Text(
                            text = user.fullName,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                    profileItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.label, color = item.color) },
                            onClick = item.action,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = item.iconRes),
                                    contentDescription = item.iconDescription,
                                    tint = item.color
                                )
                            },
                            contentPadding = PaddingValues(MaterialTheme.spacing.medium)
                        )
                    }
                } ?: CircularProgressBar()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.profile)) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = com.upsaclay.common.R.string.arrow_back_icon_description)
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Preview
@Composable
fun ProfileScreenPreview() {
    GedoiseTheme {
        Scaffold(
            topBar = { ProfileTopBar(navController = rememberNavController()) }
        ) {
            Column(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(MaterialTheme.spacing.medium),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.default_profile_picture),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .border(1.dp, Color.LightGray, CircleShape)
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Text(
                        text = userFixture.firstName + " " + userFixture.lastName,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        text = userFixture.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                profileItemsFixture.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.label,
                                color = item.color,
                                fontSize = 16.sp
                            )
                       },
                        onClick = item.action,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = item.iconDescription,
                                tint = item.color,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        contentPadding = PaddingValues(MaterialTheme.spacing.medium)
                    )
                }
            }
        }
    }
}

private val profileItemsFixture: List<ProfileItem> = listOf(
    ProfileItem(
        label = "Mon compte",
        iconRes = com.upsaclay.common.R.drawable.ic_person,
        iconDescription = "",
        action = { }
    ),
    ProfileItem(
        label = "Paramètres",
        iconRes = com.upsaclay.common.R.drawable.ic_settings,
        iconDescription = "",
        action = { }
    ),
    ProfileItem(
        label = "Support",
        iconRes = com.upsaclay.common.R.drawable.ic_support,
        iconDescription = "",
        action = { }
    ),
    ProfileItem(
        label = "Se déconnecter",
        iconRes = com.upsaclay.common.R.drawable.ic_logout,
        iconDescription = "",
        color = GedoiseColor.Error,
        action = { }
    )
)