package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.data.model.UserMapper
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: Flow<User?> = _currentUser
    private val _users = MutableStateFlow<List<User>>(emptyList())
    override val users: Flow<List<User>> = _users

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                userLocalDataSource.getCurrentUserFlow().collectLatest { userDTO ->
                    _currentUser.value = UserMapper.toDomain(userDTO)
                }
            }
            launch {
                userRemoteDataSource.getAllUsers().collectLatest {
                    _users.value = it.map { userDTO -> userDTO.toDomain() }
                }
            }
            launch {
                refreshUser()
            }
        }
    }

    override suspend fun getUser(userId: Int): User? =
        userRemoteDataSource.getUser(userId)?.toDomain()

    override suspend fun createUser(user: User): Int? {
        val userDTO = UserMapper.toDTO(user)
        val userId = userRemoteDataSource.createUser(userDTO)
        return userId?.also { userLocalDataSource.setUser(userDTO.copy(userId = userId)) }
    }

    override suspend fun updateProfilePictureUrl(
        userId: Int,
        profilePictureUrl: String
    ): Result<Unit> =
        userRemoteDataSource.updateProfilePictureUrl(userId, profilePictureUrl)
            .onSuccess { userLocalDataSource.updateProfilePictureUrl(profilePictureUrl) }

    override suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit> =
        userRemoteDataSource.deleteProfilePictureUrl(userId)
            .onSuccess { userLocalDataSource.deleteProfilePictureUrl() }

    private suspend fun refreshUser() {
        userLocalDataSource.getCurrentUser()?.let { localUser ->
            userRemoteDataSource.getCurrentUser(localUser.userId!!)?.let { remoteUser ->
                if (localUser != remoteUser) {
                    userLocalDataSource.setUser(remoteUser)
                }
            }
        }
    }
}