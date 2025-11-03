package com.upsaclay.common.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.upsaclay.common.data.UserField.Firestore.ADMIN
import com.upsaclay.common.data.UserField.Firestore.EMAIL
import com.upsaclay.common.data.UserField.Firestore.PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.UserField.Firestore.TABLE_NAME
import com.upsaclay.common.data.UserField.Server.USER_ID
import com.upsaclay.common.data.UserField.Server.USER_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.exceptions.mapFirebaseException
import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.exceptions.parseOracleException
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.remote.model.FirestoreUser
import com.upsaclay.common.data.remote.model.RemoteUserReport
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.common.data.remote.model.ServerUser
import com.upsaclay.common.data.toFirestoreUser
import com.upsaclay.common.data.toRemote
import com.upsaclay.common.data.toServerUser
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.ForbiddenException
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.net.HttpURLConnection
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class UserApiImpl(
    private val userServerApi: UserServerApi,
    private val userFirestoreApi: UserFirestoreApi
): UserApi {
    override suspend fun getUser(userId: String): User? {
        return mapFirebaseException(
            message = "Failed to get user",
            block = { userFirestoreApi.getUser(userId)?.toUser() },
        )
    }

    override fun getUserFlow(userId: String): Flow<User?> =
        userFirestoreApi.getUserFlow(userId).map { it?.toUser() }

    override suspend fun getUserWithEmail(userEmail: String): User? {
        return mapFirebaseException(
            message = "Failed to get user with email",
            block = { userFirestoreApi.getUserWithEmail(userEmail)?.toUser() }
        )
    }

    override suspend fun getUsers(): List<User> {
        return mapFirebaseException(
            message = "Failed to get users",
            block = { userFirestoreApi.getUsers().map { it.toUser() } }
        )
    }

    override suspend fun getMemberUsers(): List<User> {
        return mapFirebaseException(
            message = "Failed to get member users",
            block = { userFirestoreApi.getAdminUsers().map { it.toUser() } }
        )
    }

    override suspend fun createUser(user: User) {
        mapServerResponseException(
            message = "Failed to create user with Server",
            block = { userServerApi.createUser(user.toServerUser()) },
            specificMap = {
                val errorMessage = formatHttpError(it)
                if (it.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    throw ForbiddenException(errorMessage)
                }
                throw parseOracleException(it.body()?.code, errorMessage)
            }
        )

        mapFirebaseException(
            message = "Failed to create user with Firestore",
            block = { userFirestoreApi.createUser(user.toFirestoreUser()) }
        )
    }

    override suspend fun updateUser(user: User) {
        mapServerResponseException(
            message = "Failed to update user with Server",
            block = { userServerApi.updateUser(user.toServerUser()) }
        )
        mapFirebaseException(
            message = "Failed to update user with Firestore",
            block = { userFirestoreApi.updateUser(user.toFirestoreUser()) }
        )
    }

    override suspend fun updateProfilePictureFileName(
        userId: String,
        fileName: String
    ) {
        mapServerResponseException(
            message = "Failed to update profile picture with Server",
            block = { userServerApi.updateProfilePictureFileName(userId, fileName) }
        )

        mapFirebaseException(
            message = "Failed to update profile picture with Firestore",
            block = { userFirestoreApi.updateProfilePictureFileName(userId, fileName) }
        )
    }

    override suspend fun deleteProfilePictureFileName(userId: String) {
        mapServerResponseException(
            message = "Failed to delete profile picture with Server",
            block = { userServerApi.deleteProfilePictureFileName(userId) }
        )

        mapFirebaseException(
            message = "Failed to delete profile picture with Firestore",
            block = { userFirestoreApi.deleteProfilePictureFileName(userId) }
        )
    }

    override suspend fun reportUser(report: UserReport) {
        mapServerResponseException(
            message = "Failed to report user",
            block = { userServerApi.reportUser(report.toRemote()) }
        )
    }
}

internal interface UserServerApi {
    @POST("users/create")
    suspend fun createUser(@Body user: ServerUser): Response<ServerResponse>

    @FormUrlEncoded
    @PATCH("users/profile-picture-file-name")
    suspend fun updateProfilePictureFileName(
        @Field(USER_ID) userId: String,
        @Field(USER_PROFILE_PICTURE_FILE_NAME) userProfilePictureFileName: String
    ): Response<ServerResponse>

    @PUT("users/{userId}")
    suspend fun updateUser(@Body serverUser: ServerUser): Response<ServerResponse>

    @DELETE("users/profile-picture-file-name/{userId}")
    suspend fun deleteProfilePictureFileName(@Path("userId") userId: String): Response<ServerResponse>

    @POST("users/report")
    suspend fun reportUser(@Body report: RemoteUserReport): Response<ServerResponse>
}


internal class UserFirestoreApi {
    private val usersCollection = Firebase.firestore.collection(TABLE_NAME)

    suspend fun getUser(userId: String): FirestoreUser? = suspendCoroutine { continuation ->
        usersCollection.document(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(FirestoreUser::class.java)
                continuation.resume(user)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    fun getUserFlow(userId: String): Flow<FirestoreUser?> = callbackFlow {
        val listener = usersCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val user = it.toObject(FirestoreUser::class.java)
                    trySend(user)
                }
            }

        awaitClose { listener.remove() }
    }

    suspend fun getUserWithEmail(userEmail: String): FirestoreUser? =
        usersCollection.whereEqualTo(EMAIL, userEmail)
            .get()
            .await()
            .firstOrNull()
            .let {
                it?.toObject(FirestoreUser::class.java)
            }

    suspend fun getUsers(): List<FirestoreUser> =
        usersCollection
            .get()
            .await()
            .mapNotNull {
                it.toObject(FirestoreUser::class.java)
            }

    suspend fun getAdminUsers(): List<FirestoreUser> =
        usersCollection
            .whereEqualTo(ADMIN, true)
            .get()
            .await()
            .mapNotNull {
                it.toObject(FirestoreUser::class.java)
            }

    suspend fun createUser(firestoreUser: FirestoreUser) {
        usersCollection.document(firestoreUser.userId)
            .set(firestoreUser)
            .await()
    }

    suspend fun updateUser(firestoreUser: FirestoreUser) {
        usersCollection.document(firestoreUser.userId)
            .set(firestoreUser, SetOptions.merge())
            .await()
    }

    suspend fun updateProfilePictureFileName(userId: String, fileName: String?) {
        usersCollection.document(userId)
            .update(PROFILE_PICTURE_FILE_NAME, fileName)
            .await()
    }

    suspend fun deleteProfilePictureFileName(userId: String) {
        usersCollection.document(userId)
            .update(PROFILE_PICTURE_FILE_NAME, FieldValue.delete())
            .await()
    }
}