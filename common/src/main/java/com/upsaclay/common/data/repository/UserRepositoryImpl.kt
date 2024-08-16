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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {
    private val _user = MutableStateFlow<User?>(null)
    override val user: Flow<User> = _user.filterNotNull()
    override val currentUser: User? get() = _user.value

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _user.value = userLocalDataSource.getCurrentUser()?.let { User.fromDTO(it) }
        }
    }

    override suspend fun createUser(user: User): Result<Int> {
        val response = userRemoteDataSource.createUser(user.toDTO())

        return if (response.isSuccessful && response.body()?.data != null) {
            i(response.body()?.message ?: "User created successfully !")
            val userId = response.body()!!.data!!
            userLocalDataSource.createCurrentUser(user.copy(id = userId).toDTO())
            _user.value = user.copy(id = userId)
            Result.success(userId)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    override suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit> {
        val response = userRemoteDataSource.updateProfilePictureUrl(userId, profilePictureUrl)

        return if (response.isSuccessful) {
            userLocalDataSource.updateProfilePictureUrl(profilePictureUrl)
            _user.update { it?.copy(profilePictureUrl = profilePictureUrl) }
            user.first().profilePictureUrl?.let { i(it) }
            i("Profile picture updated successfully !")
            Result.success(Unit)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    override suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit> {
        val response = userRemoteDataSource.deleteProfilePictureUrl(userId)

        return if(response.isSuccessful) {
            userLocalDataSource.deleteProfilePictureUrl()
            _user.update { it?.copy(profilePictureUrl = null) }
            i("Profile picture deleted successfully !")
            Result.success(Unit)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            e(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }
}