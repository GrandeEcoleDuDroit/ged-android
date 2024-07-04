package com.upsaclay.core.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.upsaclay.core.ui.theme.GedoiseColor.Error

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Error,
        modifier = modifier
    )
}