package com.upsaclay.message.presentation.conversation.create

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.SearchTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.message.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateConversationScaffold(
    search: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchBackClick: () -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (!search) {
                BackTopBar(
                    onBackClick = onBackClick,
                    title = stringResource(id = R.string.new_conversation),
                    leadingIcon = {
                        IconButton(onClick = onSearchClick) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    }
                )
            } else {
                SearchTopBar(
                    query = query,
                    onQueryChange = onQueryChange,
                    onBackClick = onSearchBackClick
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        },
        content = content
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateConversationScaffoldPreview() {
    GedoiseTheme {
        Surface {
            CreateConversationScaffold(
                search = false,
                query = "",
                onQueryChange = {},
                onSearchBackClick = {},
                onBackClick = {},
                onSearchClick = {},
                snackbarHostState = SnackbarHostState(),
                content = {}
            )
        }
    }
}