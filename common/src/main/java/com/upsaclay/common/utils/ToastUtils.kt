package com.upsaclay.common.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun showToast(context: Context, @StringRes stringRes: Int) {
    Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show()
}