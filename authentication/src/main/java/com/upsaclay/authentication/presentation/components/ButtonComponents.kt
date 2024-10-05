package com.upsaclay.authentication.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.theme.GedoiseTheme

@Composable
fun LargeButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(45.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun LoadingLargeButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { },
        modifier = modifier.height(45.dp)
    ) {
        CircularProgressBar(
            color = Color.White,
            scale = 0.6f
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun LargeButtonPreview() {
    GedoiseTheme {
        LargeButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Primary Large Button",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun LoadingButtonPreview() {
    GedoiseTheme {
        LoadingLargeButton(
            modifier = Modifier.fillMaxWidth()
        )
    }
}