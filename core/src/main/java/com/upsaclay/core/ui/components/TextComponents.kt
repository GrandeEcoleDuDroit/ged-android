package com.upsaclay.core.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.core.ui.theme.GedoiseColor.ErrorColor
import com.upsaclay.core.ui.theme.GedoiseTheme

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = ErrorColor,
        modifier = modifier
    )
}

@Preview
@Composable
fun ErrorTextPreview(){
    GedoiseTheme {
        ErrorText(
            text = "Error text"
        )
    }
}