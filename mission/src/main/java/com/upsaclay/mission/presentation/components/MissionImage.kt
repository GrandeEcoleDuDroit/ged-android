package com.upsaclay.mission.presentation.components

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.LargeAsyncImage
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R

@Composable
fun MissionImage(
    modifier: Modifier = Modifier,
    model: Any?
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
            DefaultImage(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun DefaultImage(modifier: Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_default_mission_image),
            contentDescription = null,
            modifier = Modifier.size(100.dp * 1.1f)
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
private fun MissionImagePreview() {
    GedoiseTheme {
        Surface {
            MissionImage(model = "null")
        }
    }
}