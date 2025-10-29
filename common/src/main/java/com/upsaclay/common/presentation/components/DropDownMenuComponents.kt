package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.outlinedTextFieldColor
import com.upsaclay.common.utils.Phones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleSelectionDropDownMenu(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedItem: String,
    onItemClicked: (String) -> Unit,
    expanded: Boolean,
    isEnable: Boolean = true,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            enabled = isEnable,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = { onItemClicked(item) },
                    modifier = modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectionDropDownMenu(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedItems: List<String>,
    value: String,
    label: String,
    onItemClicked: (String) -> Unit,
    expanded: Boolean,
    isEnable: Boolean = true,
    singleLine: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = value,
            onValueChange = {},
            label = { Text(text = label) },
            leadingIcon = leadingIcon,
            readOnly = true,
            enabled = isEnable,
            singleLine = singleLine,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = MaterialTheme.colorScheme.outlinedTextFieldColor
        )

        ExposedDropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.smallSpacing()
                        ) {
                            Checkbox(
                                checked = selectedItems.contains(item),
                                onCheckedChange = null
                            )

                            Text(text = item)
                        }
                    },
                    onClick = { onItemClicked(item) }
                )
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun SimpleDropDownMenuPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3")
    var selectedItem by remember {
        mutableStateOf(items[0])
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    GedoiseTheme {
        Surface {
            SingleSelectionDropDownMenu(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)),
                items = items,
                selectedItem = selectedItem,
                onItemClicked = { item ->
                    selectedItem = item
                },
                expanded = expanded,
                onExpandedChange = { isExpanded ->
                    expanded = isExpanded
                },
                onDismissRequest = {
                    expanded = false
                }
            )
        }
    }
}

@Phones
@Composable
private fun MultiDropDownMenuPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3")
    var selectedItems by remember {
        mutableStateOf(listOf<String>())
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    GedoiseTheme {
        Surface {
            MultiSelectionDropDownMenu(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)),
                items = items,
                selectedItems = selectedItems,
                value = selectedItems.joinToString(" - "),
                label = "Select Items",
                onItemClicked = { item ->
                    selectedItems = if (selectedItems.contains(item)) {
                        selectedItems - item
                    } else {
                        selectedItems + item
                    }
                },
                expanded = expanded,
                onExpandedChange = { isExpanded ->
                    expanded = isExpanded
                },
                onDismissRequest = {
                    expanded = false
                }
            )
        }
    }
}