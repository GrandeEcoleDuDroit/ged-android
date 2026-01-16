package com.upsaclay.common.data.remote

import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.remote.api.user.UserApi
import com.upsaclay.common.data.toOracleUser
import com.upsaclay.common.data.toRemote
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

internal class UserRemoteDataSource(private val userApi: UserApi) {
    fun listenUser(userId: String): Flow<User?> = userApi.listenUser(userId).map { it?.toUser() }

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        try {
            userApi.getUsers()?.map { it.toUser() } ?: emptyList()
        } catch (e: Exception) {
            throw mapServerException(e)
        }
    }

    suspend fun getUser(userId: String): User? = withContext(Dispatchers.IO) {
        try {
            userApi.getUser(userId)?.toUser()
        } catch (e: Exception) {
            throw mapServerException(e)
        }
    }

    suspend fun createUser(user: User) {
        withContext(Dispatchers.IO) {
            try {
                userApi.createUser(user.toOracleUser())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun updateProfilePicture(user: User, imageFile: File, fileName: String) {
        withContext(Dispatchers.IO) {
            try {
                userApi.updateProfilePicture(user.toOracleUser(), imageFile, fileName)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun deleteProfilePicture(user: User) {
        withContext(Dispatchers.IO) {
            try {
                userApi.deleteProfilePicture(user.toOracleUser())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun reportUser(report: UserReport) {
        withContext(Dispatchers.IO) {
            try {
                userApi.reportUser(report.toRemote())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}