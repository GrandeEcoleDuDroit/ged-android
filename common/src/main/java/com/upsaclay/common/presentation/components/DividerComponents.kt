package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.listDivider
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun ListDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.listDivider
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
fun ListDividerPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    GedoiseTheme {
        Surface {
            LazyColumn {
                itemsIndexed(items) { index, text ->
                    Text(
                        modifier = Modifier.padding(MaterialTheme.padding.medium),
                        text = text
                    )
                    if (index < items.lastIndex) {
                        ListDivider()
                    }
                }
            }

        }
    }
}