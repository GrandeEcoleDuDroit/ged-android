package com.upsaclay.common.data.remote.api.user

import com.upsaclay.common.data.remote.model.FirestoreUser
import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.common.data.remote.model.RemoteUserReport
import kotlinx.coroutines.flow.Flow
import java.io.File

internal interface UserApi {
    fun listenUser(userId: String): Flow<FirestoreUser?>

    suspend fun getUsers(): List<OracleUser>?

    suspend fun getUser(userId: String): OracleUser?

    suspend fun createUser(oracleUser: OracleUser)

    suspend fun updateProfilePicture(oracleUser: OracleUser, imageFile: File, fileName: String)

    suspend fun deleteUser(oracleUser: OracleUser)

    suspend fun deleteProfilePicture(oracleUser: OracleUser)

    suspend fun reportUser(remoteUserReport: RemoteUserReport)
}