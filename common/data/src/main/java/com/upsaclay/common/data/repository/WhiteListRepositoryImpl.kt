package com.upsaclay.common.data.repository

import com.upsaclay.common.data.utils.e
import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.remote.api.WhiteListApi
import com.upsaclay.common.data.utils.sendDataServerRequest
import com.upsaclay.common.domain.repository.WhiteListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WhiteListRepositoryImpl(private val whiteListApi: WhiteListApi): WhiteListRepository {
    override suspend fun isUserWhiteListed(email: String): Boolean = withContext(Dispatchers.IO) {
        try {
            sendDataServerRequest { whiteListApi.isUserWhiteListed(email) } ?: false
        } catch (e: Exception) {
            e("Error checking if user $email is whitelisted", e)
            throw mapServerException(e)
        }
    }
}