package com.upsaclay.news.data.remote

import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.data.remote.model.AnnouncementDTO
import com.upsaclay.news.domain.model.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber.Forest.e

class AnnouncementRemoteDataSource(
    private val announcementApi: AnnouncementApi
) {
    suspend fun getAllAnnouncement(): List<Announcement> = withContext(Dispatchers.IO) {
        try {
            val announcementResponse = announcementApi.getAllAnnouncement()
            val announcementsWithUserDTO = announcementResponse.body().takeIf {
                announcementResponse.isSuccessful && it != null
            } ?: emptyList()

            announcementsWithUserDTO.map { it.toAnnouncement() }
        } catch (e: Exception){
            e("Error to get all remote announcements: %s", e.message.toString())
            emptyList()
        }
    }

    suspend fun createAnnouncement(announcement: Announcement): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val announcementDTO = AnnouncementDTO.fromAnnouncement(announcement)
            val response = announcementApi.createAnnouncement(announcementDTO)
            if(response.isSuccessful) {
                val announcementId = response.body()?.data
                announcementId?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Error to create remote announcement: id is null"))
            } else {
                val errorMessage = response.body()?.error
                Result.failure(Exception("Error to create remote announcement : $errorMessage"))
            }
        } catch (e: Exception) {
            e("Error to create remote announcement: %s", e.message)
            Result.failure(e)
        }
    }

    suspend fun deleteAnnouncement(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = announcementApi.deleteAnnouncement(id)
            if(response.isSuccessful) {
                    Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error
                Result.failure(Exception("Error to delete remote announcement : $errorMessage"))
            }
        } catch (e: Exception) {
            e("Error to delete remote announcement: %s", e.message)
            Result.failure(e)
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val announcementDTO = AnnouncementDTO.fromAnnouncement(announcement)
            val response = announcementApi.createAnnouncement(announcementDTO)
            if(response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.error
                Result.failure(Exception("Error to update remote announcement : $errorMessage"))
            }
        } catch (e: Exception) {
            e("Error to update remote announcement: %s", e.message)
            Result.failure(e)
        }
    }
}