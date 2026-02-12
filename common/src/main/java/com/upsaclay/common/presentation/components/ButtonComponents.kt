package com.upsaclay.common.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.loadingButtonColors
import com.upsaclay.common.presentation.theme.white

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = colors,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    color: IconButtonColors = IconButtonDefaults.iconButtonColors()
) {
    IconButton(
        onClick = onClick,
        colors = color
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = com.upsaclay.common.R.string.arrow_back_icon_description)
        )
    }
}

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    text: String,
    loading: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    colors: ButtonColors = MaterialTheme.colorScheme.loadingButtonColors
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = !loading && enabled,
        colors = colors
    ) {
        if (loading) {
            CircularProgressBar(
                color = MaterialTheme.colorScheme.white,
                scale = 0.5f
            )
        } else {
            Text(text = text)
        }
    }
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
private fun LoadingButtonPreview() {
    GedoiseTheme {
        LoadingButton(
            text = "Loading button",
            onClick = {},
            loading = false
        )
    }
}