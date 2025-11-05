package com.upsaclay.mission.presentation.components.formsection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.LargeAsyncImage
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.emptyImageBackground
import com.upsaclay.common.presentation.theme.emptyImageForeground
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R

@Composable
fun MissionImageFormSection(
    modifier: Modifier = Modifier,
    imageUri: String?,
    onImageClick: () -> Unit,
    onRemoveImageClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(dimensionResource(R.dimen.mission_image_height))
            .clickable(onClick = onImageClick),
        contentAlignment = Alignment.Center
    ) {
        imageUri?.let {
            NonEmptyImage(
                modifier = Modifier.fillMaxSize(),
                imageUri = it,
                onRemoveImageClick = onRemoveImageClick
            )
        } ?: EmptyImage()
    }
}

@Composable
private fun EmptyImage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.emptyImageBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(80.dp),
            painter = painterResource(R.drawable.ic_outline_add_image),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.emptyImageForeground
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)))

        Text(
            text = stringResource(R.string.add_image),
            color = MaterialTheme.colorScheme.emptyImageForeground
        )
    }
}

@Composable
private fun NonEmptyImage(
    modifier: Modifier = Modifier,
    imageUri: String,
    onRemoveImageClick: () -> Unit
) {
    Box(modifier = modifier) {
        LargeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUri
        )

        IconButton(
            modifier = Modifier
                .padding(dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding))
                .align(Alignment.TopEnd)
                .clip(ShapeDefaults.ExtraLarge)
                .background(Color.Black.copy(alpha = 0.5f))
                .size(dimensionResource(R.dimen.mission_image_remove_button_size)),
            onClick = onRemoveImageClick
        ) {
            Icon(
                modifier = Modifier.size(dimensionResource(R.dimen.mission_image_remove_button_icon_size)),
                imageVector = Icons.Default.Clear,
                contentDescription = "Delete Image",
                tint = Color.White
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateMissionImageSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionImageFormSection(
                imageUri = null,
                onImageClick = {},
                onRemoveImageClick = {}
            )
        }
    }
}