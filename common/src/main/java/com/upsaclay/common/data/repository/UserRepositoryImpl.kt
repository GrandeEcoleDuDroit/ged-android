package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUserFlow: Flow<User> = _currentUser.filterNotNull()
    override val currentUser: User? get() = _currentUser.value

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                userLocalDataSource.getCurrentUserFlow().collect { userDTO ->
                    _currentUser.value = userDTO.toDomain()
                }
            }
            launch {
                refreshUser()
            }
        }
    }

    override suspend fun getUser(userId: Int): User? {
        return userRemoteDataSource.getUser(userId)?.toDomain()
    }

    override suspend fun getAllUsers(): List<User> {
        return userRemoteDataSource.getAllUsers().map { it.toDomain() }
    }

    override suspend fun getOnlineUsers(): List<User> {
        return userRemoteDataSource.getOnlineUsers().map { it.toDomain() }
    }

    override suspend fun createUser(user: User): Int? {
        val userDTO = UserDTO.fromDomain(user)
        val userId = userRemoteDataSource.createUser(userDTO)
        return userId?.also { userLocalDataSource.setUser(userDTO.copy(userId = userId)) }
    }

    override suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit> {
        return userRemoteDataSource.updateProfilePictureUrl(userId, profilePictureUrl)
            .onSuccess { userLocalDataSource.updateProfilePictureUrl(profilePictureUrl) }
    }

    override suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit> {
        return userRemoteDataSource.deleteProfilePictureUrl(userId)
            .onSuccess { userLocalDataSource.deleteProfilePictureUrl() }
    }

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
