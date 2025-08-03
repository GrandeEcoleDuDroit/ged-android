package com.upsaclay.common.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.cursor
import com.upsaclay.common.presentation.theme.inputBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaticSearchBar(
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.search),
    query: String,
    onQueryChange: (String) -> Unit,
    onResetQuery: () -> Unit = { onQueryChange("") },
    textStyle: TextStyle = TextStyle.Default,
    leadingIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null
        )
    },
    trailingIcon: @Composable () -> Unit = {
        if (query.isNotEmpty()) {
            IconButton(onClick = onResetQuery) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
    }
) {
    BasicTextField(
        modifier = modifier,
        value = query,
        onValueChange = onQueryChange,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.cursor)
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            innerTextField = innerTextField,
            value = query,
            placeholder = { Text(text = placeholder) },
            shape = ShapeDefaults.ExtraLarge,
            enabled = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.inputBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.inputBackground,
                cursorColor = MaterialTheme.colorScheme.cursor
            ),
            contentPadding = PaddingValues(),
            singleLine = true,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() }
        )
    }
}

/**
 * =====================================================================
 *                                 Preview
 * =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    var query by remember { mutableStateOf("") }
    GedoiseTheme {
        Box(
            modifier = Modifier.padding(dimensionResource(R.dimen.small_padding))
        ) {
            StaticSearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = query,
                onQueryChange = { query = it }
            )
        }
    }
}