package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.e
import com.upsaclay.common.utils.formatHttpError
import com.upsaclay.common.utils.i
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.IOException

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUserFlow: Flow<User> = _currentUser.filterNotNull()
    override val currentUser: User? = _currentUser.value

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                userLocalDataSource.getCurrentUserFlow().collect { user ->
                    _currentUser.value = user
                }
            }
            launch {
                refreshUser()
            }
        }
    }

    override suspend fun getAllUser(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(userId: Int): User? {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: User): Result<Int> {
        val response = userRemoteDataSource.createUser(user)

        return if (response.isSuccessful && response.body()?.data != null) {
            val userId = response.body()!!.data!!
            userLocalDataSource.setUser(user.copy(id = userId))
            i(response.body()?.message ?: "User created successfully !")
            Result.success(userId)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response)
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    override suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit> {
        val response = userRemoteDataSource.updateProfilePictureUrl(userId, profilePictureUrl)

        return if (response.isSuccessful) {
            userLocalDataSource.updateProfilePictureUrl(profilePictureUrl)
            i(response.body()?.message ?: "Profile picture url updated successfully !")
            Result.success(Unit)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response)
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    override suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit> {
        val response = userRemoteDataSource.deleteProfilePictureUrl(userId)

        return if(response.isSuccessful) {
            userLocalDataSource.deleteProfilePictureUrl()
            i(response.body()?.message ?: "Profile picture url deleted successfully !")
            Result.success(Unit)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response)
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    private suspend fun refreshUser() {
        userLocalDataSource.getCurrentUser()?.let { localUser ->
            userRemoteDataSource.getUser(localUser.id)
                .onSuccess { remoteUser ->
                    remoteUser?.let {
                        val shouldBeUpdated =
                            remoteUser.profilePictureUrl != localUser.profilePictureUrl ||
                                    remoteUser.isMember != localUser.isMember ||
                                    remoteUser.schoolLevel != localUser.schoolLevel
                        if (shouldBeUpdated) {
                            userLocalDataSource.setUser(remoteUser)
                        }
                    }
                }
                .onFailure { exception ->
                    e(exception.message ?: "Error retrieving remote user")
                }
        }
    }
}
