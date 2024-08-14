package com.upsaclay.common.data.local

import com.upsaclay.common.data.model.UserDTO

class UserLocalDataSource(private val userDataStore: UserDataStore) {
    suspend fun createCurrentUser(user: UserDTO) {
        userDataStore.storeCurrentUser(user)
    }

    suspend fun getCurrentUser(): UserDTO? {
        return userDataStore.getCurrentUser()
    }

    suspend fun updateProfilePictureUrl(profilePictureUrl: String) {
        val user = userDataStore.getCurrentUser()
        user?.let {
            userDataStore.storeCurrentUser(it.copy(userProfilePictureUrl = profilePictureUrl))
        }
    }

    suspend fun hasDefaultProfilePicture(): Boolean {
        return userDataStore.getUserHasDefaultProfilePicture()
    }

    suspend fun setUserHasDefaultProfilePicture(hasDefaultProfilePicture: Boolean) {
        userDataStore.storeUserHasDefaultProfilePicture(hasDefaultProfilePicture)
    }
}