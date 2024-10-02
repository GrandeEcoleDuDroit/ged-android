package com.upsaclay.common.domain.repository

import android.net.Uri

interface DrawableRepository {
    fun getDrawableUri(drawableId: Int): Uri?
}