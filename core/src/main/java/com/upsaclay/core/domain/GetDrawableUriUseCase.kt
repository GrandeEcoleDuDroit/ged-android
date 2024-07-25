package com.upsaclay.core.domain

import android.content.Context
import android.net.Uri

class GetDrawableUriUseCase(private val context: Context) {
    operator fun invoke(drawableId: Int): Uri {
        return Uri.parse("android.resource://${context.packageName}/$drawableId")
    }
}