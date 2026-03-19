package com.upsaclay.mission.presentation.missiondetails.allusers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.presentation.components.SearchTopBar
import com.upsaclay.common.presentation.components.UserItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.presentation.AllUsersMissionDetailsPreviewParameterProvider
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AllUsersDestination(
    users: List<User>,
    onBackClick: () -> Unit,
    onUserClick: (User) -> Unit,
    viewModel: AllUsersViewModel = koinViewModel(
        parameters = { parametersOf(users) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    AllUsersScreen(
        users = uiState.users,
        userQuery = uiState.userQuery,
        onUserQueryChange = viewModel::onUserQueryChange,
        onResetQuery = viewModel::onResetUserQuery,
        onBackClick = onBackClick,
        onUserClick = onUserClick
    )
}

@Composable
private fun AllUsersScreen(
    users: List<User>,
    userQuery: String,
    onUserQueryChange: (String) -> Unit,
    onResetQuery: () -> Unit,
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                query = userQuery,
                onQueryChange = onUserQueryChange,
                onClearClick = onResetQuery,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(users) { user ->
                UserItem(
                    modifier = Modifier.clickable { onUserClick(user) },
                    user = user
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

@PhonePreviews
@Composable
private fun AllUsersScreenPreview(
    @PreviewParameter(AllUsersMissionDetailsPreviewParameterProvider::class) users: List<User>
) {
    GedoiseTheme {
        Surface {
            AllUsersScreen(
                users = users,
                userQuery = "",
                onUserQueryChange = {},
                onResetQuery = {},
                onBackClick = {},
                onUserClick = {}
            )
        }
    }
}