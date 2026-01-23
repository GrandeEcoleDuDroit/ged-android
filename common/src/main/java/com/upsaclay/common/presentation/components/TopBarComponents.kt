package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import com.upsaclay.common.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.topBarTitle
import com.upsaclay.common.presentation.theme.white
import com.upsaclay.common.utils.PhonePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopBar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.topBarTitle,
                maxLines = 1
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    title: String,
    leadingIcon: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.topBarTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            BackButton(onClick = onBackClick)
        },
        actions = leadingIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    buttonEnable: Boolean = true,
    actionLabel: String,
    onCancelClick: () -> Unit,
    onActionClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.topBarTitle,
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
        actions = {
            Button(
                modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding)),
                enabled = buttonEnable,
                contentPadding = PaddingValues(
                    vertical = dimensionResource(R.dimen.default_padding),
                    horizontal = dimensionResource(R.dimen.small_medium_padding)
                ),
                colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.white),
                onClick = onActionClick
            ) {
                Text(text = actionLabel)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onClearClick: () -> Unit

) {
    CenterAlignedTopAppBar(
        title = {
            StaticSearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = query,
                onQueryChange = onQueryChange,
                leadingIcon = {
                    BackButton(
                        onClick = onBackClick,
                        color = IconButtonDefaults.iconButtonColors().copy(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = onClearClick,
                            colors = IconButtonDefaults.iconButtonColors().copy(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun TitleTopBarPreview() {
    GedoiseTheme {
        Surface {
            TitleTopBar(title = "Title")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PhonePreviews
@Composable
private fun BackTopBarPreview() {
    GedoiseTheme {
        Surface {
            BackTopBar(
                onBackClick = {},
                title = "Title"
            )
        }
    }
}

@PhonePreviews
@Composable
private fun EditTopBarPreview() {
    GedoiseTheme {
        Surface {
            EditTopBar(
                title = "Title",
                onCancelClick = { },
                onActionClick = { },
                actionLabel = "Enregister"
            )
        }
    }
}

@PhonePreviews
@Composable
private fun SearchTopBarPreview() {
    GedoiseTheme {
        Surface {
            SearchTopBar(
                query = "",
                onQueryChange = {},
                onBackClick = {},
                onClearClick = {}
            )
        }
    }
}