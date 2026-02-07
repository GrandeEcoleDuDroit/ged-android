package com.upsaclay.common.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.R
import com.upsaclay.common.extension.extraSmallSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.closeFABContainerColor
import com.upsaclay.common.presentation.theme.closeFABContentColor
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

@Composable
fun FloatingActionButtonMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    buttonIcon: @Composable () -> Unit,
    onButtonClick: () -> Unit,
    onCloseClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val radius = if (expanded) dimensionResource(R.dimen.fab_size) / 2f else dimensionResource(R.dimen.fab_corner_size)
    val cornerRadius by animateDpAsState(targetValue = radius)
    val containerColor = if(expanded) MaterialTheme.colorScheme.closeFABContainerColor else FloatingActionButtonDefaults.containerColor
    val contentColor = if(expanded) MaterialTheme.colorScheme.closeFABContentColor else contentColorFor(FloatingActionButtonDefaults.containerColor)

    Column(
        modifier = modifier.clip(FloatingActionButtonDefaults.shape),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut(animationSpec = tween(durationMillis = 100)) + shrinkOut()
        ) {
            Column(
                verticalArrangement = Arrangement.extraSmallSpacing(),
                horizontalAlignment = Alignment.End
            ) {
                content()
            }
        }

        FloatingActionButton(
            shape = RoundedCornerShape(cornerRadius),
            containerColor = containerColor,
            contentColor = contentColor,
            onClick = if (expanded) onCloseClick else onButtonClick
        ) {
            if (expanded) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null
                )
            } else {
                buttonIcon()
            }
        }
    }
}


@Composable
fun FabMenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        shape = CircleShape,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.smallSpacing()
        ) {
            icon()
            text()
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
        Surface {
            PrimaryButton(
                text = "Primary Button",
                onClick = {},
                enabled = false
            )
        }
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

@Preview
@Composable
private fun FloatingActionButtonMenuPreview() {
    var expanded by remember { mutableStateOf(false) }

    GedoiseTheme {
        FloatingActionButtonMenu(
            expanded = expanded,
            buttonIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            },
            onButtonClick = { expanded = true },
            onCloseClick = { expanded = false }
        ) {
            FabMenuItem(
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                text = { Text(text = "Add") }
            )

            FabMenuItem(
                onClick = {},
                icon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = null
                    )
                },
                text = { Text(text = "Email") }
            )
        }
    }
}