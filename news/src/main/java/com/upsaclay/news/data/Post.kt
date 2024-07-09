package com.upsaclay.news.data

import android.graphics.Bitmap

data class Post(
    val title: String,
    val url: String,
    val picture: Bitmap?
)