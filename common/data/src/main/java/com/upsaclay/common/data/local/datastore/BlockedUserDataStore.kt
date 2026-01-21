package com.upsaclay.common.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.data.extensions.getGsonFlowValue
import com.upsaclay.common.data.extensions.getGsonValue
import com.upsaclay.common.data.extensions.removeValue
import com.upsaclay.common.data.extensions.setGsonValue
import com.upsaclay.common.domain.entity.BlockedUser
import com.upsaclay.common.domain.entity.BlockedUsers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BlockedUserDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "blocked_user")
    private val store = context.dataStore
    private val blockedUserKey = stringPreferencesKey("blockedUserKey")

    fun getBlockedUsersFlow(): Flow<BlockedUsers> =
        store.getGsonFlowValue<BlockedUsers>(blockedUserKey).map { it ?: emptyMap() }

    suspend fun getBlockedUsers(): BlockedUsers = store.getGsonValue(blockedUserKey) ?: emptyMap()

    suspend fun addBlockedUser(blockedUser: BlockedUser) {
        val blockedUsers = getBlockedUsers().toMutableMap()
        blockedUsers[blockedUser.userId] = blockedUser
        store.setGsonValue(blockedUserKey, blockedUsers)
    }

    suspend fun removeBlockUser(userId: String) {
        val blockedUsers = getBlockedUsers().toMutableMap()
        blockedUsers.remove(userId)
        store.setGsonValue(blockedUserKey, blockedUsers)
    }

    suspend fun deleteBlockedUsers() {
        store.removeValue(blockedUserKey)
    }
}