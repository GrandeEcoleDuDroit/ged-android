package com.upsaclay.common.domain.extensions


fun String.capitalize(): String = replaceFirstChar { it.uppercase() }

fun String.capitalizeWords(): String =
    replace(Regex("(?<!\\p{L})\\p{L}")) {
        it.value.uppercase()
    }
