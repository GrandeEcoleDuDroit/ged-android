package com.upsaclay.common.data.local

import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserLocalDataSource(private val userDataStore: UserDataStore) {
    suspend fun createUser(user: User) {
        userDataStore.storeUser(UserDTO.fromDomain(user))
    }

    fun getUser(): Flow<User?> = userDataStore.getUserFlow().map { it?.toDomain() }

    suspend fun updateProfilePictureUrl(profilePictureUrl: String) {
        userDataStore.getUser()?.let { userDTO ->
            userDataStore.storeUser(userDTO.copy(userProfilePictureUrl = profilePictureUrl))
        }
    }

    suspend fun deleteProfilePictureUrl() {
        userDataStore.getUser()?.let { userDTO ->
            userDataStore.storeUser(userDTO.copy(userProfilePictureUrl = null))
        }
    }
}