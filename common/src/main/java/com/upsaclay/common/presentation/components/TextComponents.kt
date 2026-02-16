package com.upsaclay.common.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.R
import com.upsaclay.common.domain.loremIpsum
import com.upsaclay.common.extension.extraSmallSpacing
import com.upsaclay.common.extension.noRippleClickable
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.informationText

@Composable
fun EmptyText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        style = textStyle,
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

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 3,
    style: TextStyle = LocalTextStyle.current
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var displayShowMoreText by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.extraSmallSpacing()
    ) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (!expanded && it.hasVisualOverflow) {
                    displayShowMoreText = true
                }
            },
            style = style
        )

        if (displayShowMoreText) {
            Text(
                modifier = Modifier.noRippleClickable {
                    displayShowMoreText = false
                    expanded = true
                },
                text = stringResource(R.string.show_more_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmptyTextPreview() {
    GedoiseTheme {
        Surface {
            EmptyText(text = "Empty text")
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SectionTitlePreview() {
    GedoiseTheme {
        Surface {
            SectionTitle(
                title = "Section title"
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExpandableTextPreview() {
    GedoiseTheme {
        Surface {
            ExpandableText(
                text = loremIpsum(),
                maxLines = 2
            )
        }
    }
}