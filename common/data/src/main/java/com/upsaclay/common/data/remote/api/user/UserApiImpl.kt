package com.upsaclay.common.data.remote.api.user

import com.upsaclay.common.data.utils.sendDataServerRequest
import com.upsaclay.common.data.utils.sendServerRequest
import com.upsaclay.common.data.remote.model.FirestoreUser
import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.common.data.remote.model.RemoteUserReport
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

internal class UserApiImpl(
    private val userServerApi: UserServerApi,
    private val userFirestoreApi: UserFirestoreApi
): UserApi {
    override fun listenUser(userId: String): Flow<FirestoreUser?> =
        userFirestoreApi.listenUser(userId)

    override suspend fun getUsers(): List<OracleUser>? {
        return sendDataServerRequest {
            userServerApi.getUsers()
        }
    }

    override suspend fun getUser(userId: String): OracleUser? {
        return sendDataServerRequest {
            userServerApi.getUser(userId)
        }
    }

    override suspend fun createUser(oracleUser: OracleUser) {
        sendServerRequest { userServerApi.createUser(oracleUser) }
    }

    override suspend fun updateProfilePicture(oracleUser: OracleUser, imageFile: File, fileName: String) {
        val imagePart = MultipartBody.Part.createFormData("image", fileName, imageFile.asRequestBody("image/*".toMediaType()))
        val userIdPart = oracleUser.userId.toRequestBody("text/plain".toMediaType())
        val previousProfilePictureFileNamePart = oracleUser.userProfilePictureFileName?.toRequestBody("text/plain".toMediaType())

        sendServerRequest {
            userServerApi.updateProfilePicture(imagePart, userIdPart, previousProfilePictureFileNamePart)
        }
    }

    override suspend fun deleteProfilePicture(oracleUser: OracleUser) {
        sendServerRequest {
            userServerApi.deleteProfilePicture(oracleUser.userId, oracleUser.userProfilePictureFileName!!)
        }
    }

    override suspend fun reportUser(remoteUserReport: RemoteUserReport) {
        sendServerRequest {
            userServerApi.reportUser(remoteUserReport)
        }
    }
}