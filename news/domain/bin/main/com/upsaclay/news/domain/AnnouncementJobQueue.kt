package com.upsaclay.news.domain

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AnnouncementJobQueue {
    private var jobs = mutableMapOf<String, Job>()
    private val mutex = Mutex()

    suspend fun addJob(job: Job, announcementId: String) {
        mutex.withLock {
            jobs[announcementId] = job
        }
    }

    suspend fun cancelAndRemoveJob(announcementId: String) {
        mutex.withLock {
            jobs[announcementId]?.cancel()
            jobs.remove(announcementId)
        }
    }
}