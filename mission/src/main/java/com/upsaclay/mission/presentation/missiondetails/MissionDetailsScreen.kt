package com.upsaclay.mission.presentation.missiondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.extension.extraSmallSpacing
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.UserItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.backButtonBackground
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.components.MissionImage
import com.upsaclay.mission.presentation.components.item.ManagerItem
import com.upsaclay.mission.presentation.components.item.MissionInformationItem
import com.upsaclay.mission.presentation.components.item.SectionTitle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MissionDetailsDestination(
    onBackClick: () -> Unit,
    missionId: Int,
    onManagerClick: (User) -> Unit,
    onParticipantClick: (User) -> Unit,
    viewModel: MissionDetailsViewModel = koinViewModel(
        parameters = { parametersOf(missionId) }
    )
) {
    val uiState = viewModel.uiState.collectAsState()

    if (
        uiState.value.mission != null &&
        uiState.value.registrationDisabled != null
    ) {
        MissionDetailsScreen(
            onBackClick = onBackClick,
            mission = uiState.value.mission!!,
            registerButtonEnabled = uiState.value.registrationDisabled!!,
            onRegisterClick = viewModel::registerToMission,
            onManagerClick = onManagerClick,
            onParticipantClick = onParticipantClick
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressBar()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MissionDetailsScreen(
    onBackClick: () -> Unit,
    mission: Mission,
    registerButtonEnabled: Boolean,
    onRegisterClick: () -> Unit,
    onManagerClick: (User) -> Unit,
    onParticipantClick: (User) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val contentStyle = MaterialTheme.typography.bodyMedium

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                contentPadding = PaddingValues()
            ) {
                PrimaryButton(
                    modifier = Modifier
                        .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                        .fillMaxWidth(),
                    text = stringResource(R.string.register),
                    enable = registerButtonEnabled,
                    onClick = onRegisterClick
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                MissionImage(
                    modifier = Modifier.height(dimensionResource(R.dimen.mission_image_height)),
                    model = when (val state = mission.state) {
                        is MissionState.Draft -> state.imageUri
                        is MissionState.Publishing -> state.imagePath
                        is MissionState.Published -> state.imageUrl
                        is MissionState.Error -> state.imagePath
                    }
                )

                Column(
                    modifier = Modifier.padding(vertical = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                    verticalArrangement = Arrangement.mediumSpacing()
                ) {
                    TitleAndDescriptionSection(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        mission = mission
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                    )

                    InformationSection(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        mission = mission,
                        textStyle = contentStyle
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                    )

                    ManagerSection(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        managers = mission.managers,
                        textStyle = contentStyle,
                        onManagerClick = onManagerClick
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                    )

                    ParticipantSection(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        users = mission.participants,
                        textStyle = contentStyle,
                        onParticipantClick = onParticipantClick
                    )

                    if (mission.tasks.isNotEmpty()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                        )

                        TaskSection(
                            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                            tasks = mission.tasks,
                            textStyle = contentStyle
                        )
                    }
                }
            }

            MissionTopBar(
                modifier = Modifier.align(Alignment.TopStart),
                scrollBehavior = scrollBehavior,
                title = mission.title,
                onBackClick = onBackClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MissionTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onBackClick: () -> Unit
) {
    if (scrollBehavior.state.contentOffset.dp <= dimensionResource(R.dimen.image_top_bar_offset)) {
        DefaultTopBar(
            modifier = modifier,
            title = title,
            onBackClick = onBackClick
        )
    } else {
        ImageTopBar(
            modifier = modifier,
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun ImageTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.backButtonBackground
            ),
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(com.upsaclay.common.R.string.arrow_back_icon_description)
            )
        }
    }
}

@Composable
private fun DefaultTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(com.upsaclay.common.R.string.arrow_back_icon_description)
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TitleAndDescriptionSection(
    modifier: Modifier = Modifier,
    mission: Mission
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallMediumSpacing()
    ) {
        Text(
            text = mission.title,
            style = titleStyle
        )

        Text(
            text = mission.description,
            style = descriptionStyle
        )
    }
}

@Composable
private fun InformationSection(
    modifier: Modifier = Modifier,
    mission: Mission,
    textStyle: TextStyle
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallMediumSpacing()
    ) {
        SectionTitle(title = stringResource(R.string.information))

        MissionInformationItem(
            modifier = Modifier.fillMaxWidth(),
            mission = mission,
            textStyle = textStyle
        )
    }
}

@Composable
private fun ManagerSection(
    modifier: Modifier = Modifier,
    managers: List<User>,
    textStyle: TextStyle,
    onManagerClick: (User) -> Unit
) {
    Column(verticalArrangement = Arrangement.extraSmallSpacing()) {
        SectionTitle(
            modifier = modifier,
            title = stringResource(R.string.managers)
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            items(managers) {
                ManagerItem(
                    modifier = Modifier.clickable { onManagerClick(it) },
                    user = it,
                    imageScale = 0.4f,
                    showAdminIndicator = false,
                    textStyle = textStyle
                )
            }
        }
    }
}

@Composable
private fun ParticipantSection(
    modifier: Modifier = Modifier,
    users: List<User>,
    textStyle: TextStyle,
    onParticipantClick: (User) -> Unit
) {
    Column(verticalArrangement = Arrangement.extraSmallSpacing()) {
        SectionTitle(
            modifier = modifier,
            title = stringResource(R.string.participants)
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            if (users.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.no_participants),
                        style = textStyle,
                        color = MaterialTheme.colorScheme.informationText,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(users) {
                    UserItem(
                        modifier = Modifier.clickable { onParticipantClick(it) },
                        user = it,
                        imageScale = 0.4f,
                        textStyle = textStyle
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskSection(
    modifier: Modifier = Modifier,
    tasks: List<MissionTask>,
    textStyle: TextStyle
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        SectionTitle(title = stringResource(R.string.tasks))

        Column(
            verticalArrangement = Arrangement.smallMediumSpacing()
        ) {
            tasks.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.smallSpacing()
                ) {
                    Text(text = "\u2022", fontSize = 20.sp)
                    Text(text = it.value, style = textStyle)
                }
            }
        }
    }
}

private val titleStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.titleLarge

private val descriptionStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.bodyLarge

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun MissionDetailsScreenPreview() {
    GedoiseTheme {
        Surface {
            MissionDetailsScreen(
                mission = missionFixture,
                registerButtonEnabled = true,
                onRegisterClick = {},
                onBackClick = {},
                onManagerClick = {},
                onParticipantClick = {}
            )
        }
    }
}