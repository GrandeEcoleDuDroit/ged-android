package com.upsaclay.gedoise.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.upsaclay.common.ui.theme.GedoiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GedoiseTheme {
                Navigation()
            }
        }
    }
}