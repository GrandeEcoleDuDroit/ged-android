package com.upsaclay.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.core.ui.theme.GedoiseTheme

@Composable
fun InfiniteCircularProgressIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(42.dp),
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

@Preview
@Composable
private fun InfiniteCircularProgressIndicatorPreview() {
    GedoiseTheme {
        InfiniteCircularProgressIndicator()
    }
}
