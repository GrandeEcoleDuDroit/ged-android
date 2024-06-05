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
fun InfiniteCircularProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(40.dp),
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewProgressIndicator() {
    GedoiseTheme {
        InfiniteCircularProgressIndicator()
    }
}
