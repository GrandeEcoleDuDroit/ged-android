package com.upsaclay.common.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {
    CircularProgressIndicator(
        modifier = modifier.size(42.dp * scale),
        strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth * (0.7f * scale),
        trackColor = GedoiseColor.InputBackground
    )
}

@Preview
@Composable
private fun InfiniteCircularProgressIndicatorPreview() {
    GedoiseTheme {
        CircularProgressBar()
    }
}
