package com.upsaclay.common.extension

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.upsaclay.common.presentation.theme.padding

@Composable
fun Modifier.rootMediumPadding(innerPadding: PaddingValues): Modifier {
    return this.padding(
        top = innerPadding.calculateTopPadding(),
        start = MaterialTheme.padding.medium,
        end = MaterialTheme.padding.medium,
        bottom = innerPadding.calculateBottomPadding() +
                MaterialTheme.padding.medium
    )
}

@Composable
fun Modifier.rootMediumPadding(): Modifier {
    return this.padding(
        start = MaterialTheme.padding.medium,
        end = MaterialTheme.padding.medium,
        bottom = MaterialTheme.padding.medium
    )
}