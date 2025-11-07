package com.upsaclay.common.domain.extensions

inline fun <T>List<T>.replace(predicate: (T) -> Boolean, value: T): List<T> =
    map { if (predicate(it)) value else it }