package com.upsaclay.common.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun CoroutineScope.executeUiBlockingRequest(
    block: suspend () -> Unit,
    onLoading: () -> Unit,
    onError: suspend (Exception) -> Unit,
    onFinished: () -> Unit
) {
    var loadingJob: Job? = null

    launch {
        try {
            loadingJob = launch {
                delay(300)
                onLoading()
            }

            block()
        } catch (e: Exception) {
            onError(e)
        } finally {
            loadingJob?.cancel()
            onFinished()
        }
    }
}