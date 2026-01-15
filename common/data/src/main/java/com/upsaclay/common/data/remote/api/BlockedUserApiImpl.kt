package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.BlockedUserField.Remote.BLOCKED_USER_ID
import com.upsaclay.common.data.BlockedUserField.Remote.USER_ID
import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.model.RemoteBlockedUser
import com.upsaclay.common.data.remote.model.ServerResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal class BlockedUserApiImpl(
    private val blockedServerApi: BlockedUserServerApi
): BlockedUserApi {
    override suspend fun getBlockedUsers(currentUserId: String): List<RemoteBlockedUser>? {
        return mapServerResponseException(
            block = { blockedServerApi.getBlockedUsers(currentUserId) },
            message = "Failed to get blocked users"
        )
    }

    override suspend fun blockUser(remoteBlockedUser: RemoteBlockedUser) {
        mapServerResponseException(
            block = { blockedServerApi.addBlockedUser(remoteBlockedUser.userId, remoteBlockedUser.blockedUserId) },
            message = "Failed to block user"
        )
    }

    override suspend fun unblockUser(currentUserId: String, blockedUserId: String) {
        mapServerResponseException(
            block = { blockedServerApi.removeBlockedUser(currentUserId, blockedUserId) },
            message = "Failed to unblock user"
        )
    }
}

internal interface BlockedUserServerApi {
    @GET("blocked-users/{currentUserId}")
    suspend fun getBlockedUsers(@Path("currentUserId") currentUserId: String): Response<List<RemoteBlockedUser>>

    @FormUrlEncoded
    @POST("blocked-users/create")
    suspend fun addBlockedUser(
        @Field(USER_ID) currentUserId: String,
        @Field(BLOCKED_USER_ID) blockedUserId: String
    ): Response<ServerResponse>

    @FormUrlEncoded
    @POST("blocked-users/delete")
    suspend fun removeBlockedUser(
        @Field(USER_ID) currentUserId: String,
        @Field(BLOCKED_USER_ID) blockedUserId: String
    ): Response<ServerResponse>
}