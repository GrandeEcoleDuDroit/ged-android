package com.upsaclay.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.R
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme

@Composable
fun PrimaryLargeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(45.dp)
    ) {
        Text(
            text = text,
        )
    }
}

@Composable
fun SmallFAB(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    colorButton: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = CircleShape,
    colorIcon: Color = GedoiseColor.White,
    onClick: () -> Unit
) {
    SmallFloatingActionButton(
        modifier = modifier,
        containerColor = colorButton,
        contentColor = colorIcon,
        shape = shape,
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun SmallShowButton(
    modifier: Modifier = Modifier,
    @StringRes textId: Int = R.string.show,
    onClick: () -> Unit
){
    Button(
        modifier = modifier.size(width = 50.dp, height = 25.dp),
        shape = ShapeDefaults.Small,
        colors = ButtonColors(
            containerColor = GedoiseColor.ButtonShowColor,
            contentColor = GedoiseColor.Black,
            disabledContentColor = GedoiseColor.Grey,
            disabledContainerColor = GedoiseColor.ButtonShowColor
        ),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun PrimaryLargeButtonPreview() {
    GedoiseTheme {
        PrimaryLargeButton(
            "Primary Large Button",
            {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun SmallFABPreview() {
    GedoiseTheme {
        SmallFAB(
            icon = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.icon_add_descrption),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SmallShowButtonPreview() {
    GedoiseTheme {
        SmallShowButton(
            onClick = {}
        )
    }
}