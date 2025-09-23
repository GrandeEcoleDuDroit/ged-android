package com.upsaclay.common.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.data.extensions.getGsonFlowValue
import com.upsaclay.common.data.extensions.getGsonValue
import com.upsaclay.common.data.extensions.setGsonValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BlockedUserDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "blocked_user")
    private val store = context.dataStore
    private val blockedUserKey = stringPreferencesKey("blockedUserKey")

    fun getBlockedUserIdsFlow(): Flow<Set<String>> =
        store.getGsonFlowValue<Set<String>>(blockedUserKey).map { it ?: emptySet() }

    suspend fun getBlockedUserIds(): Set<String> = store.getGsonValue(blockedUserKey) ?: emptySet()

    suspend fun blockUser(userId: String) {
        val blockedUserIds = getBlockedUserIds().toMutableSet()
        blockedUserIds.add(userId)
        store.setGsonValue(blockedUserKey, blockedUserIds)
    }

    suspend fun unblockUser(userId: String) {
        val blockedUserIds = getBlockedUserIds().toMutableSet()
        blockedUserIds.remove(userId)
        store.setGsonValue(blockedUserKey, blockedUserIds)
    }
}