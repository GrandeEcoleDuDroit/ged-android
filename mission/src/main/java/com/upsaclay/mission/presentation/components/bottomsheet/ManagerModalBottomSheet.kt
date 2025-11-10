package com.upsaclay.mission.presentation.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.StaticSearchBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.presentation.components.item.MissionUserItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectManagerModalBottomSheet(
    users: List<User>,
    selectedManagers: List<User>,
    userQuery: String,
    onUserQueryChange: (String) -> Unit,
    onResetQuery: () -> Unit,
    onSaveClick: (List<User>) -> Unit,
    onDismissRequest: () -> Unit
) {
    var currentSelectedManagers by remember { mutableStateOf(selectedManagers) }
    var saveEnabled by remember { mutableStateOf(false) }
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = state
    ) {
        StaticSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.medium_padding)),
            query = userQuery,
            onQueryChange = onUserQueryChange,
            onResetQuery = onResetQuery,
            placeholder = stringResource(R.string.search_ellipsis)
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.medium_padding))
                .weight(1f)
        ) {
            if (users.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.small_padding))
                            .fillMaxWidth(),
                        text = stringResource(id = com.upsaclay.common.R.string.no_user),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.previewText
                    )
                }
            } else {
                items(users) { user ->
                    val selected = currentSelectedManagers.contains(user)
                    SelectableManagerItem(
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

        TextButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            onClick = { onSaveClick(currentSelectedManagers) },
            enabled = saveEnabled
        ) {
            Text(
                text = stringResource(R.string.save),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))
    }
}

@Composable
private fun SelectableManagerItem(
    user: User,
    selected: Boolean,
    onUserClick: (User) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onUserClick(user) })
            .padding(
                horizontal = dimensionResource(R.dimen.medium_padding),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.smallSpacing()
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = null
        )

        MissionUserItem(
            user = user,
            imageScale = 0.5f
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

    GedoiseTheme {
        Surface {
            SelectManagerModalBottomSheet(
                users = usersFixture + usersFixture,
                selectedManagers = listOf(userFixture),
                userQuery = query,
                onSaveClick = {},
                onUserQueryChange = { query = it },
                onResetQuery = { query = "" },
                onDismissRequest = {}
            )
        }
    }
}