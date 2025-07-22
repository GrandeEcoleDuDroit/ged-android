package com.upsaclay.forum.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectManagerModalBottomSheet(
    users: List<User>,
    selectedManagers: List<User>,
    userQuery: String,
    onUserQueryChange: (String) -> Unit,
    onResetQuery: () -> Unit,
    onSaveClick: (List<User>) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var currentSelectedManagers by remember { mutableStateOf(selectedManagers) }
    var saveEnabled by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column {
            TextButton(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .align(Alignment.End),
                onClick = { onSaveClick(currentSelectedManagers) },
                enabled = saveEnabled,
                content = { Text(text = stringResource(R.string.save)) }
            )

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                query = userQuery,
                onQueryChange = onUserQueryChange,
                onSearch = {},
                active = false,
                onActiveChange = {},
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_ellipsis),
                        color = MaterialTheme.colorScheme.inputForeground
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (userQuery.isNotEmpty()) {
                        IconButton(onClick = onResetQuery) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                },
                content = {}
            )

            LazyColumn {
                items(users) { user ->
                    val selected = currentSelectedManagers.contains(user)
                    SelectManagerItem(
                        user = user,
                        selected = selected,
                        onUserClick = {
                            currentSelectedManagers = if (selected) {
                                currentSelectedManagers - user
                            } else {
                                currentSelectedManagers + user
                            }
                            saveEnabled = currentSelectedManagers.isNotEmpty() &&
                                    currentSelectedManagers != selectedManagers
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectManagerItem(
    user: User,
    selected: Boolean,
    onUserClick: (User) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onUserClick(user) })
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.smallMedium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.mediumSpacing()
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = null
        )

        ProfilePicture(
            url = user.profilePictureUrl,
            scale = 0.5f
        )

        Text(
            text = user.fullName,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun SelectManagerModalBottomSheetPreview() {
    var query by remember { mutableStateOf("") }
    var selectedUsers by remember { mutableStateOf(listOf(userFixture)) }

    GedoiseTheme {
        Surface {
            SelectManagerModalBottomSheet(
                users = usersFixture,
                selectedManagers = selectedUsers,
                userQuery = query,
                onSaveClick = {},
                onUserQueryChange = { query = it },
                onResetQuery = {},
                onDismiss = {}
            )
        }
    }
}