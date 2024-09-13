package com.upsaclay.common.data.local

import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal class UserLocalDataSource(private val userDataStore: UserDataStore) {

    fun getUser(): Flow<User> = userDataStore.getUserFlow().mapNotNull { it?.toDomain() }

    suspend fun setUser(user: User) {
        userDataStore.storeUser(UserDTO.fromDomain(user))
    }

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