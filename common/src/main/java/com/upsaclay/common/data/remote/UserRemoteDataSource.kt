package com.upsaclay.common.data.remote

import com.google.firebase.firestore.FirebaseFirestoreException
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.data.remote.api.RemoteUserFirebase
import com.upsaclay.common.data.remote.api.UserFirebaseApi
import com.upsaclay.common.data.remote.api.UserRetrofitApi
import com.upsaclay.common.utils.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

internal class UserRemoteDataSource(
    private val userRetrofitApi: UserRetrofitApi,
    private val userFirebaseApi: UserFirebaseApi
) {
    suspend fun getCurrentUser(userId: Int): UserDTO? {
        return withContext(Dispatchers.IO) {
            try {
                val response = userRetrofitApi.getUser(userId)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    e("Error getting current user : ${response.message()}")
                    null
                }
            } catch (e: IOException) {
                e("Error getting current user: ${e.message.toString()}")
                null
            }
        }
    }

    suspend fun getUser(userId: Int): RemoteUserFirebase? {
        return withContext(Dispatchers.IO) {
            try {
                userFirebaseApi.getUser(userId.toString())
            } catch (e: IOException) {
                e("Error getting user: ${e.message.toString()}")
                null
            } catch (e: FirebaseFirestoreException) {
                e("Error getting user: ${e.message.toString()}")
                null
            }
        }
    }

    suspend fun getAllUsers(): List<RemoteUserFirebase> {
        return try {
            userFirebaseApi.getAllUsers()
        } catch (e: Exception) {
            e("Error getting all users: ${e.message.toString()}", e)
            emptyList()
        }
    }

    suspend fun getOnlineUsers(): List<RemoteUserFirebase> {
        return try {
            userFirebaseApi.getOnlineUsers()
        } catch (e: IOException) {
            e("Error getting all online users: ${e.message.toString()}", e)
            emptyList()
        }
        catch (e: FirebaseFirestoreException) {
            e("Error getting all online users: ${e.message.toString()}", e)
            emptyList()
        }
    }

    suspend fun createUser(userDTO: UserDTO): Int? {
        return withContext(Dispatchers.IO) {
            try {
                val response = userRetrofitApi.createUser(userDTO)
                if(response.isSuccessful && response.body()?.data != null) {
                    userFirebaseApi.createUser(RemoteUserFirebase.fromDTO(userDTO))
                    response.body()?.data!!
                } else {
                    null
                }
            } catch (e: IOException) {
                e("Error creating user: ${e.message.toString()}", e)
                null
            }
        }
    }

    suspend fun updateProfilePictureUrl(userId: Int, newProfilePictureUrl: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                userRetrofitApi.updateProfilePictureUrl(userId, newProfilePictureUrl)
                userFirebaseApi.updateProfilePictureUrl(userId.toString(), newProfilePictureUrl)
            } catch (e: IOException) {
                e("Error updating user profile picture url: ${e.message.toString()}", e)
                Result.failure(e)
            } catch (e: FirebaseFirestoreException) {
                e("Error updating user profile picture url: ${e.message.toString()}", e)
                Result.failure(e)
            }
        }
    }

    suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                userRetrofitApi.deleteProfilePictureUrl(userId)
                userFirebaseApi.updateProfilePictureUrl(userId.toString(), null)
            } catch (e: IOException) {
                e("Error deleting user profile picture url: ${e.message.toString()}", e)
                Result.failure(e)
            } catch (e: FirebaseFirestoreException) {
                e("Error deleting user profile picture url: ${e.message.toString()}", e)
                Result.failure(e)
            }
        }
    }
}