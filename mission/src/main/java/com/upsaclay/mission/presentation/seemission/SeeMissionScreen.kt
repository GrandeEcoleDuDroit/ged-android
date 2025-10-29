package com.upsaclay.mission.presentation.seemission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.noRippleClickable
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.backButtonBackground
import com.upsaclay.common.presentation.theme.missionContent
import com.upsaclay.common.presentation.theme.missionTitle
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.components.MissionImage
import com.upsaclay.mission.presentation.components.item.HorizontalManagerItem
import com.upsaclay.mission.presentation.components.item.MissionInformationItem
import com.upsaclay.mission.presentation.components.item.SectionTitle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeeMissionDestination(
    onBackClick: () -> Unit,
    missionId: Int,
    onManagerClick: (User) -> Unit,
    viewModel: SeeMissionViewModel = koinViewModel(
        parameters = { parametersOf(missionId) }
    )
) {
    val uiState = viewModel.uiState.collectAsState()

    if (uiState.value.mission != null) {
        SeeMissionScreen(
            onBackClick = onBackClick,
            mission = uiState.value.mission!!,
            onRegisterClick = {},
            onManagerClick = onManagerClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeeMissionScreen(
    onBackClick: () -> Unit,
    mission: Mission,
    onRegisterClick: () -> Unit,
    onManagerClick: (User) -> Unit
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
                    modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                    verticalArrangement = Arrangement.mediumSpacing()
                ) {
                    TitleAndDescriptionSection(mission = mission)

                    HorizontalDivider()

                    InformationSection(
                        mission = mission,
                        textStyle = contentStyle
                    )

                    HorizontalDivider()

                    ManagerSection(
                        managers = mission.managers,
                        textStyle = contentStyle,
                        onManagerClick = onManagerClick
                    )

                    if (mission.tasks.isNotEmpty()) {
                        HorizontalDivider()

                        TaskSection(
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
        ContentTopBar(
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
private fun TitleAndDescriptionSection(mission: Mission) {
    Column(verticalArrangement = Arrangement.smallMediumSpacing()) {
        Text(
            text = mission.title,
            style = MaterialTheme.typography.missionTitle
        )

        Text(
            text = mission.description,
            style = MaterialTheme.typography.missionContent
        )
    }
}

@Composable
private fun InformationSection(
    mission: Mission,
    textStyle: TextStyle
) {
    Column(verticalArrangement = Arrangement.smallMediumSpacing()) {
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
    managers: List<User>,
    textStyle: TextStyle,
    onManagerClick: (User) -> Unit
) {
    Column(verticalArrangement = Arrangement.smallMediumSpacing()) {
        SectionTitle(title = stringResource(R.string.managers))

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp),
            verticalArrangement = Arrangement.smallMediumSpacing()
        ) {
            items(managers) {
                HorizontalManagerItem(
                    modifier = Modifier.noRippleClickable { onManagerClick(it) },
                    user = it,
                    imageScale = 0.4f,
                    textStyle = textStyle
                )
            }
        }
    }
}

@Composable
private fun TaskSection(
    tasks: List<MissionTask>,
    textStyle: TextStyle
) {
    Column(verticalArrangement = Arrangement.smallSpacing()) {
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
private fun ContentTopBar(
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

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun SeeMissionScreenPreview() {
    GedoiseTheme {
        SeeMissionScreen(
            mission = missionFixture,
            onRegisterClick = {},
            onBackClick = {},
            onManagerClick = {}
        )
    }
}