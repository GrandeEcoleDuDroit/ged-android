package com.upsaclay.common.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun Drawable.toBitmap(): Bitmap{
    if(this is BitmapDrawable)
        return this.bitmap

    val bitmap = Bitmap.createBitmap(
        this.intrinsicWidth,
        this.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.setBounds(0,0,canvas.width,canvas.height)
    this.draw(canvas)

    return bitmap
}

fun formatProfilePictureFileName(userId: Int): String =
    "$userId-profile-picture.jpg"

fun formatHttpError(message: String, errorBody: String?): String {
    val body = errorBody ?: "No error body"
    return "Error request : $message\n" +
            "Body: $body"
}

fun formatProfilePictureUrl(userId: Int): String {
    return "https://objectstorage.eu-paris-1.oraclecloud.com/n/ax5bfuffglob/b/bucket-gedoise/o/$userId-profile-picture.jpeg"
}