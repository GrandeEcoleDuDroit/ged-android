package com.upsaclay.common.data.remote

import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.remote.api.BlockedUserApi
import com.upsaclay.common.data.toBlockedUser
import com.upsaclay.common.data.toRemote
import com.upsaclay.common.data.utils.sendDataServerRequest
import com.upsaclay.common.data.utils.sendServerRequest
import com.upsaclay.common.domain.entity.BlockedUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BlockedUserRemoteDataSource(private val blockedUserApi: BlockedUserApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getBlockedUsers(currentUserId: String): List<BlockedUser> {
        return withContext(dispatcher) {
            try {
                sendDataServerRequest {
                    blockedUserApi.getBlockedUsers(currentUserId)
                }?.map { it.toBlockedUser() } ?: emptyList()
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun addBlockedUser(currentUserId: String, blockedUser: BlockedUser) {
        val remoteBlockedUser = blockedUser.toRemote(currentUserId)
        withContext(dispatcher) {
            try {
                sendServerRequest {
                    blockedUserApi.addBlockedUser(currentUserId, remoteBlockedUser.blockedUserId)
                }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun removeBlockedUser(currentUserId: String, blockedUserId: String) {
        withContext(dispatcher) {
            try {
                sendServerRequest {
                    blockedUserApi.removeBlockedUser(currentUserId, blockedUserId)
                }
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}