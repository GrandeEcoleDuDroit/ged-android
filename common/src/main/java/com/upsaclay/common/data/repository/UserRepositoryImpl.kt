package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.errorLog
import com.upsaclay.common.utils.formatHttpError
import com.upsaclay.common.utils.infoLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {
    private val _userFlow = MutableStateFlow<User?>(null)
    override val userFlow: Flow<User> = _userFlow.filterNotNull()
    override val user: User?
        get() = _userFlow.value

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _userFlow.value = userLocalDataSource.getCurrentUser()?.let { User.fromDTO(it) }
        }
    }

    override suspend fun createUser(user: User): Result<Int> {
        val response = userRemoteDataSource.createUser(user.toDTO())

        return if (response.isSuccessful && response.body()?.data != null) {
            infoLog(response.body()?.message ?: "User created successfully !")
            val userId = response.body()!!.data
            userLocalDataSource.createCurrentUser(user.copy(id = userId).toDTO())
            _userFlow.value = user.copy(id = userId)
            Result.success(userId)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            errorLog(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    override suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit> {
        val response = userRemoteDataSource.updateProfilePictureUrl(userId, profilePictureUrl)
        return if (response.isSuccessful) {
            infoLog("Profile picture updated successfully !")
            userLocalDataSource.updateProfilePictureUrl(profilePictureUrl)
            _userFlow.update { it?.copy(profilePictureUrl = profilePictureUrl) }
            Result.success(Unit)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            errorLog(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }
}