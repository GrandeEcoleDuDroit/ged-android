package com.upsaclay.common.data.remote

import com.upsaclay.common.data.model.ServerResponse
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.data.remote.api.UserRemoteApi
import com.upsaclay.common.utils.errorLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

internal class UserRemoteDataSource(private val userRemoteApi: UserRemoteApi) {
    suspend fun createUser(user: UserDTO): Response<ServerResponse.IntServerResponse> = withContext(Dispatchers.IO) {
        try {
            userRemoteApi.createUser(user)
        } catch (e: Exception) {
            errorLog("Error to create user: ${e.message.toString()}")
            Response.error(500, e.message.toString().toResponseBody())
        }
    }

    suspend fun updateProfilePictureUrl(userId: Int, newProfilePictureUrl: String): Response<ServerResponse.EmptyServerResponse> {
        return withContext(Dispatchers.IO) {
            try {
                userRemoteApi.updateProfilePictureUrl(userId, newProfilePictureUrl)
            } catch (e: Exception) {
                errorLog("Error to update user profile picture url: ${e.message.toString()}", e)
                Response.error(500, e.message.toString().toResponseBody())
            }
        }
    }

    suspend fun deleteProfilePicture(imageName: String): Response<ServerResponse.EmptyServerResponse> {
        return withContext(Dispatchers.IO) {
            try {
                userRemoteApi.deleteProfilePicture(imageName)
            } catch (e: Exception) {
                errorLog("Error to delete user profile picture: ${e.message.toString()}", e)
                Response.error(500, e.message.toString().toResponseBody())
            }
        }
    }
}