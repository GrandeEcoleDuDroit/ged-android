package com.upsaclay.common.data.local

import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLocalDataSource(private val userDataStore: UserDataStore) {
    suspend fun createUser(user: User) {
        userDataStore.storeUser(UserDTO.fromUser(user))
    }

    suspend fun getUser(): Flow<User?> = userDataStore.getUserFlow().map { it?.toUser() }

    suspend fun updateProfilePictureUrl(profilePictureUrl: String) {
        val userDTO = userDataStore.getUser()
        userDTO?.let {
            userDataStore.storeUser(it.copy(userProfilePictureUrl = profilePictureUrl))
        }
    }

    suspend fun deleteProfilePictureUrl() {
        val userDTO = userDataStore.getUser()
        userDTO?.let {
            userDataStore.storeUser(it.copy(userProfilePictureUrl = null))
        }
    }
}