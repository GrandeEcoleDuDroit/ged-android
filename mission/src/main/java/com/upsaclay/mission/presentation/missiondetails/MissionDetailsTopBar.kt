package com.upsaclay.mission.presentation.missiondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.imageIconButtonColors
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MissionTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    if (scrollBehavior.state.contentOffset.dp <= dimensionResource(R.dimen.image_top_bar_offset)) {
        DefaultTopBar(
            modifier = modifier,
            title = title,
            onBackClick = onBackClick,
            onOptionClick = onOptionClick
        )
    } else {
        ImageTopBar(
            modifier = modifier,
            onBackClick = onBackClick,
            onOptionClick = onOptionClick
        )
    }
}

@Composable
private fun ImageTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            colors = MaterialTheme.colorScheme.imageIconButtonColors,
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(com.upsaclay.common.R.string.arrow_back_icon_description)
            )
        }

        OptionButton(
            color = MaterialTheme.colorScheme.imageIconButtonColors,
            onClick = onOptionClick
        )
    }
}

@Composable
private fun DefaultTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
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

        OptionButton(
            onClick = onOptionClick
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
private fun ImageTopBarPreview() {
    GedoiseTheme {
        Surface {
            ImageTopBar(
                onBackClick = {},
                onOptionClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun DefaultTopBarPreview() {
    GedoiseTheme {
        Surface {
            DefaultTopBar(
                title = "Mission",
                onBackClick = {},
                onOptionClick = {}
            )
        }
    }
}