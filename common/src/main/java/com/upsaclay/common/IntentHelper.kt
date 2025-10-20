package com.upsaclay.common

import android.content.Context
import android.content.Intent

interface IntentHelper {
    fun getMainActivityIntent(context: Context): Intent
}