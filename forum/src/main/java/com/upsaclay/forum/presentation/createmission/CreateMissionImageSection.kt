package com.upsaclay.forum.presentation.createmission

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.presentation.components.MissionImage

@Composable
fun CreateMissionImageSection(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    onImageClick: () -> Unit,
    onRemoveImageClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(200.dp)
            .clickable(onClick = onImageClick),
        contentAlignment = Alignment.Center
    ) {

        MissionImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUri,
            imageScale = 1.5f
        )

        IconButton(
            modifier = Modifier
                .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                .align(Alignment.TopEnd)
                .clip(ButtonDefaults.filledTonalShape)
                .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.8f)),
            onClick = onRemoveImageClick
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Image",
                tint = Color.White
            )

        }
    }
}

@Phones
@Composable
private fun CreateMissionImageSectionPreview() {
    GedoiseTheme {
        Surface {
            CreateMissionImageSection(
                imageUri = null,
                onImageClick = {},
                onRemoveImageClick = {}
            )
        }
    }
}