package com.upsaclay.common.data.local

import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal class UserLocalDataSource(private val userDataStore: UserDataStore) {

    fun getCurrentUserFlow(): Flow<User> = userDataStore.getCurrentUserFlow().mapNotNull { it?.toDomain() }

    suspend fun getCurrentUser(): User? = userDataStore.getCurrentUser()?.toDomain()

    suspend fun setUser(user: User) {
        userDataStore.storeCurrentUser(UserDTO.fromDomain(user))
    }

    suspend fun updateProfilePictureUrl(profilePictureUrl: String) {
        userDataStore.getCurrentUser()?.let { userDTO ->
            userDataStore.storeCurrentUser(userDTO.copy(userProfilePictureUrl = profilePictureUrl))
        }
    }

    suspend fun deleteProfilePictureUrl() {
        userDataStore.getCurrentUser()?.let { userDTO ->
            userDataStore.storeCurrentUser(userDTO.copy(userProfilePictureUrl = null))
        }
    }
}