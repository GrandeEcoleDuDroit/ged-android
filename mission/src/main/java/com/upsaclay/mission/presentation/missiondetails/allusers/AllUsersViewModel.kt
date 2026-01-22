package com.upsaclay.mission.presentation.missiondetails.allusers

import androidx.lifecycle.ViewModel
import com.upsaclay.common.domain.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AllUsersViewModel(
    private val users: List<User>
): ViewModel() {
    private val _uiState = MutableStateFlow(AllUsersUiState())
    val uiState: StateFlow<AllUsersUiState> = _uiState
    private var defaultUsers: List<User> = emptyList()

    init {
        initUiState()
    }

    fun onUserQueryChange(query: String) {
        _uiState.update {
            it.copy(userQuery = query)
        }

        filterUsersByName(query)
    }

    fun onResetUserQuery() {
        _uiState.update {
            it.copy(
                userQuery = "",
                users = defaultUsers
            )
        }
    }

    private fun filterUsersByName(query: String) {
        val users = if (query.isNotBlank()) {
            defaultUsers.filter { user ->
                user.firstName.contains(query, ignoreCase = true) ||
                        user.lastName.contains(query, ignoreCase = true)
            }
        } else {
            defaultUsers
        }

        _uiState.update {
            it.copy(users = users)
        }
    }

    private fun initUiState() {
        defaultUsers = users
        _uiState.update {
            it.copy(users = users)
        }
    }

    data class AllUsersUiState(
        val users: List<User> = emptyList(),
        val userQuery: String = "",
    )
}