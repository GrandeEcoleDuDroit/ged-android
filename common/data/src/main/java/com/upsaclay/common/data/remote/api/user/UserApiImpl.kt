package com.upsaclay.common.data.remote.api.user

import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.exceptions.parseOracleException
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.toOracleUser
import com.upsaclay.common.data.toRemote
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.ForbiddenException
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.HttpURLConnection

internal class UserApiImpl(
    private val userServerApi: UserServerApi,
    private val userFirestoreApi: UserFirestoreApi
): UserApi {
    override suspend fun getUsers(): List<User> {
        return mapServerResponseException(
            message = "Failed to get users",
            block = { userServerApi.getUsers() }
        )?.map { it.toUser() } ?: emptyList()
    }

    override suspend fun getUser(userId: String): User? {
        return mapServerResponseException(
            message = "Failed to get user",
            block = { userServerApi.getUser(userId) },
        )?.toUser()
    }

    override fun listenUser(userId: String): Flow<User?> =
        userFirestoreApi.listenUser(userId).map { it?.toUser() }

    override suspend fun createUser(user: User) {
        mapServerResponseException(
            message = "Failed to create user",
            block = { userServerApi.createUser(user.toOracleUser()) },
            specificMap = {
                val errorMessage = formatHttpError(it)
                if (it.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    throw ForbiddenException(errorMessage)
                }
                throw parseOracleException(it.body()?.code, errorMessage)
            }
        )
    }

    override suspend fun updateProfilePicture(user: User, imageFile: File, fileName: String) {
        val oracleUser = user.toOracleUser()
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            fileName,
            imageFile.asRequestBody("image/*".toMediaType())
        )
        val userIdPart = oracleUser.userId.toRequestBody("text/plain".toMediaType())
        val previousProfilePictureFileNamePart = oracleUser.userProfilePictureFileName?.toRequestBody("text/plain".toMediaType())


        mapServerResponseException(
            message = "Failed to update profile picture",
            block = { userServerApi.updateProfilePicture(imagePart, userIdPart, previousProfilePictureFileNamePart) }
        )
    }

    override suspend fun deleteProfilePicture(user: User) {
        val oracleUser = user.toOracleUser()
        mapServerResponseException(
            message = "Failed to delete profile picture",
            block = { userServerApi.deleteProfilePicture(oracleUser.userId, oracleUser.userProfilePictureFileName!!) }
        )
    }

    override suspend fun reportUser(report: UserReport) {
        mapServerResponseException(
            message = "Failed to report user",
            block = { userServerApi.reportUser(report.toRemote()) }
        )
    }
}