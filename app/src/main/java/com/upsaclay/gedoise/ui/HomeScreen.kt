package com.upsaclay.gedoise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController
) {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "Hello Gedoise!", style = MaterialTheme.typography.titleLarge)
    }
}