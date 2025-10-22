package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enable,
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            tint = Color.Gray,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun SimpleFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    FloatingActionButton (
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        content = icon
    )

}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun PrimaryButtonPreview() {
    GedoiseTheme {
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Primary Button",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun OptionButtonPreview() {
    GedoiseTheme {
        OptionButton(
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SimpleFloatingActionButtonPreview() {
    GedoiseTheme {
        SimpleFloatingActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    null
                )
            },
            onClick = {}
        )
    }
}