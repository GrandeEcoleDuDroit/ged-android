package com.upsaclay.forum.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.LargeAsyncImage
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.forum.R

@Composable
fun MissionImage(
    modifier: Modifier = Modifier,
    model: Any? = null,
    imageScale: Float = 1f
) {
    model?.let {
        LargeAsyncImage(
            modifier = modifier,
            model = it
        )
    } ?: run {
        DefaultImage(
            modifier = modifier,
            imageScale = imageScale
        )
    }
}

@Composable
private fun DefaultImage(
    modifier: Modifier,
    imageScale: Float
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_default_mission_image),
            contentDescription = null,
            modifier = Modifier.size(100.dp * imageScale)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MissionImagePreview() {
    GedoiseTheme {
        Surface {
            MissionImage()
        }
    }
}