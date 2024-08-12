package com.upsaclay.common.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.ui.theme.GedoiseTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDropDownMenu(
    modifier: Modifier = Modifier,
    items: ImmutableList<String>,
    selectedItem: String,
    onItemClicked: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
){
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = modifier
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier
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

@Preview(showBackground = true)
@Composable
fun MyDropDownMenuPreview(){
    val items = persistentListOf("BUT 1", "BUT 2", "BUT 3")
    var selectedItem by remember {
        mutableStateOf(items[0])
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    GedoiseTheme {
        MyDropDownMenu(
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
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}