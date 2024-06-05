package com.upsaclay.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.ui.theme.PrimaryColor

@Composable
fun PrimaryLargeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
fun SmallFAB(
    icon: ImageVector,
    contentDescription: String,
    colorButton: Color,
    colorIcon: Color,
    onClick: () -> Unit
) {
    SmallFloatingActionButton(
        containerColor = colorButton,
        contentColor = colorIcon,
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@Preview
@Composable
fun PreviewButtons() {
    GedoiseTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PrimaryLargeButton("Primary Large Button", {})
            Spacer(Modifier.height(10.dp))
            SmallFAB(
                icon = Icons.Default.Add,
                contentDescription = "Add",
                colorButton = PrimaryColor,
                colorIcon = Color.White,
                onClick = {}
            )
        }
    }
}
