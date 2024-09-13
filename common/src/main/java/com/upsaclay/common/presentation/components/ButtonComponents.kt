package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme

@Composable
fun PrimaryLargeButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(45.dp)
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun PrimaryLargeButtonPreview() {
    GedoiseTheme {
        PrimaryLargeButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Primary Large Button",
            onClick = {},
        )
    }
}