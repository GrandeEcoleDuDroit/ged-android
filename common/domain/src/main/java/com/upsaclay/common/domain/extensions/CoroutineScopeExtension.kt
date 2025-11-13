package com.upsaclay.common.domain.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun CoroutineScope.launchDelayed(delayMillis: Long, block: suspend () -> Unit) = launch {
    delay(delayMillis)
    block()
}
