package com.upsaclay.common.data.repository

import com.upsaclay.common.data.utils.e
import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import java.io.File

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    scope: CoroutineScope
) : UserRepository {
    private val _user = userLocalDataSource.getUserFlow()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    override val user: Flow<User> = _user.filterNotNull()
    override val currentUser: User?
        get() = _user.value

    override fun getUserFlow(userId: String): Flow<User?> = userRemoteDataSource.listenUser(userId)

    override suspend fun getUsers(): List<User> {
        return try {
            userRemoteDataSource.getUsers()
        } catch (e: Exception) {
            e("Error getting remote users", e)
            throw e
        }
    }

    override suspend fun getUser(userId: String): User? {
        return try {
            userRemoteDataSource.getUser(userId)
        } catch (e: Exception) {
            e("Error getting remote user $userId", e)
            throw e
        }
    }

    override suspend fun getCurrentUser(): User? = userLocalDataSource.getUser()

    override suspend fun createUser(user: User) {
        try {
            userRemoteDataSource.createUser(user)
            userLocalDataSource.storeUser(user)
        } catch (e: Exception) {
            e("Error creating user ${user.id}", e)
            throw e
        }
    }

    override suspend fun storeUser(user: User) {
        userLocalDataSource.storeUser(user)
    }

    override suspend fun updateProfilePicture(user: User, imageFile: File, fileName: String) {
        try {
            userRemoteDataSource.updateProfilePicture(user, imageFile, fileName)
            userLocalDataSource.updateProfilePictureFileName(fileName)
        } catch (e: Exception) {
            e("Error updating profile picture for user ${user.id}", e)
            throw e
        }
    }

    override suspend fun deleteLocalUser() {
        userLocalDataSource.removeUser()
    }

    override suspend fun deleteProfilePicture(user: User) {
        try {
            userRemoteDataSource.deleteProfilePicture(user)
            userLocalDataSource.updateProfilePictureFileName(null)
        } catch (e: Exception) {
            e("Error deleting profile picture for user ${user.id}", e)
            throw e
        }
    }

    override suspend fun reportUser(report: UserReport) {
        try {
            userRemoteDataSource.reportUser(report)
        } catch (e: Exception) {
            e("Error reporting user ${report.reportedUser.id}", e)
            throw e
        }
    }
}