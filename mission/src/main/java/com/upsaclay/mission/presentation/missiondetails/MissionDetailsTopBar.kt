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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.imageIconButtonColors
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.utils.PhonePreviews

@Composable
internal fun MissionDetailsTopBar(
    modifier: Modifier = Modifier,
    title: String,
    showTitleTopBar: Boolean,
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    if (showTitleTopBar) {
        TextTopBar(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(MaterialTheme.padding.extraSmall)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            colors = MaterialTheme.colorScheme.imageIconButtonColors.copy(
                contentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor
            ),
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(com.upsaclay.common.R.string.arrow_back_icon_description)
            )
        }

        OptionButton(
            colors = MaterialTheme.colorScheme.imageIconButtonColors.copy(
                contentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor
            ),
            onClick = onOptionClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.padding.extraSmall)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(com.upsaclay.common.R.string.arrow_back_icon_description)
                )
            }

            Text(
                modifier = Modifier.padding(start = MaterialTheme.padding.extraSmall),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = TopAppBarDefaults.topAppBarColors().titleContentColor
            )
        }

        OptionButton(
            onClick = onOptionClick,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor
            )
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun ImageTopBarPreview() {
    GedoiseTheme {
         ImageTopBar(
            onBackClick = {},
            onOptionClick = {}
        )
    }
}

@PhonePreviews
@Composable
private fun TextTopBarPreview() {
    GedoiseTheme {
        Surface {
            TextTopBar(
                title = "Mission",
                onBackClick = {},
                onOptionClick = {}
            )
        }
    }
}