package com.upsaclay.mission.domain

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MissionJobQueue {
    private var jobs = mutableMapOf<String, Job>()
    private val mutex = Mutex()

    suspend fun addJob(job: Job, missionId: String) {
        mutex.withLock {
            jobs[missionId] = job
        }
    }

    suspend fun cancelAndRemoveJob(missionId: String) {
        mutex.withLock {
            jobs[missionId]?.cancel()
            jobs.remove(missionId)
        }
    }
}