package com.upsaclay.mission.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.LargeAsyncImage
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.defaultImageForeground
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun MissionImage(
    modifier: Modifier = Modifier,
    model: Any?,
    defaultImageScale: Float = 1.4f
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        model?.let {
            LargeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = it
            )
        } ?: run {
            DefaultImage(
                modifier = Modifier.fillMaxSize(),
                scale = defaultImageScale
            )
        }
    }
}

@Composable
private fun DefaultImage(
    modifier: Modifier,
    scale: Float
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_target),
            contentDescription = null,
            modifier = Modifier.size(100.dp * scale),
            tint = MaterialTheme.colorScheme.defaultImageForeground
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
private fun MissionImagePreview() {
    GedoiseTheme {
        Surface {
            MissionImage(model = null)
        }
    }
}