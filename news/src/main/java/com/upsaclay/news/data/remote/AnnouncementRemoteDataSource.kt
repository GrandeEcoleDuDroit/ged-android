package com.upsaclay.news.data.remote

import com.upsaclay.common.utils.formatHttpError
import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.data.remote.model.RemoteAnnouncement
import com.upsaclay.news.domain.model.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber.Forest.e
import java.io.IOException

internal class AnnouncementRemoteDataSource(
    private val announcementApi: AnnouncementApi
) {
    suspend fun getAllAnnouncement(): List<Announcement> = withContext(Dispatchers.IO) {
        try {
            val response = announcementApi.getAllAnnouncement()
            if(response.isSuccessful) {
                val announcementsWithUserDTO = response.body().takeIf {
                    it != null
                } ?: emptyList()
                announcementsWithUserDTO.map { it.toDomain() }
            } else {
                val errorMessage = formatHttpError("Error retrieving all remote announcement", response)
                e(errorMessage)
                emptyList()
            }

        } catch (e: Exception){
            e("Error retrieving all remote announcements: ${e.message}")
            emptyList()
        }
    }

    suspend fun createAnnouncement(announcement: Announcement): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val remoteAnnouncement = RemoteAnnouncement.fromDomain(announcement)
            val response = announcementApi.createAnnouncement(remoteAnnouncement)
            if(response.isSuccessful) {
                val announcementId = response.body()?.data

                announcementId?.let { id ->
                    Result.success(id)
                } ?: run {
                    val errorMessage = response.body()?.error ?: "Error creating remote announcement: id is null"
                    e(errorMessage)
                    Result.failure(IOException(errorMessage))
                }
            } else {
                val errorMessage = formatHttpError("Error creating remote announcement", response)
                e(errorMessage)
                Result.failure(IOException(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = "Error creating remote announcement: ${e.message}"
            e(errorMessage)
            Result.failure(e)
        }
    }

    suspend fun deleteAnnouncement(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = announcementApi.deleteAnnouncement(id)
            if(response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMessage = formatHttpError("Error deleting remote announcement", response)
                e(errorMessage)
                Result.failure(IOException(errorMessage))
            }
        } catch (e: Exception) {
            e("Error deleting remote announcement: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val remoteAnnouncement = RemoteAnnouncement.fromDomain(announcement)
            val response = announcementApi.createAnnouncement(remoteAnnouncement)
            if(response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMessage = formatHttpError("Error updating remote announcement", response)
                e(errorMessage)
                Result.failure(IOException(errorMessage))
            }
        } catch (e: Exception) {
            e("Error updating remote announcement: ${e.message}")
            Result.failure(e)
        }
    }
}