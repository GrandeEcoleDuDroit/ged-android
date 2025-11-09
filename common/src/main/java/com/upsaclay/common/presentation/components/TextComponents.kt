package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.utils.Phones

@Composable
fun EmptyText(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.informationText,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextIcon(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.smallSpacing()
    ) {
        icon()
        text()
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun EmptyTextPreview() {
    GedoiseTheme {
        Surface {
            EmptyText("Empty text")
        }
    }
}

@Phones
@Composable
private fun TextIconPreview() {
    GedoiseTheme {
        Surface {
            TextIcon(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null
                    )
                },
                text = { Text("Text") }
            )
        }
    }
}