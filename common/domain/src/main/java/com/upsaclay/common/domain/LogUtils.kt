package com.upsaclay.common.domain

import timber.log.Timber

fun Any.d(message: String) {
    Timber.tag(javaClass.simpleName).d(message)
}

fun Any.e(message: String, throwable: Throwable? = null) {
    Timber.tag(javaClass.simpleName).e(throwable, message)
}

fun Any.i(message: String) {
    Timber.tag(javaClass.simpleName).i(message)
}