package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {
    CircularProgressIndicator(
        modifier = modifier.size(42.dp * scale),
        strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth * (0.9f * scale)
    )
}

@Composable
fun LinearProgressBar(
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        modifier = modifier
    )
}

@Preview
@Composable
private fun CircularProgressBarPreview() {
    GedoiseTheme {
        CircularProgressBar()
    }
}

@Preview
@Composable
private fun LinearProgressBarPreview() {
    GedoiseTheme {
        LinearProgressBar()
    }
}
