package com.upsaclay.authentication.data.remote

import com.upsaclay.common.utils.e
import com.upsaclay.common.utils.formatHttpError
import com.upsaclay.common.utils.i
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

internal class AuthenticationRemoteDataSource(
    private val parisSaclayAuthenticationApi: ParisSaclayAuthenticationApi,
) {
    suspend fun login(
        email: String,
        password: String,
        hash: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        i("Logging in with Paris-Saclay...")
        try {
            val response = parisSaclayAuthenticationApi.login(email, password, hash)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMessage = formatHttpError("Error logging in with Paris-Saclay", response)
                e(errorMessage)
                Result.failure(IOException(errorMessage))
            }
        } catch (e: Exception) {
            e("Error logging in with Paris-Saclay: ${e.message}")
            Result.failure(e)
        }
    }
}