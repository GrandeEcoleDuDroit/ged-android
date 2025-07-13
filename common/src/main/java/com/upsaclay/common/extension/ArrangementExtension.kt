package com.upsaclay.common.extension

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.upsaclay.common.presentation.theme.spacing

@Composable
fun Arrangement.smallSpacing(): Arrangement.HorizontalOrVertical = Arrangement.spacedBy(MaterialTheme.spacing.small)

@Composable
fun Arrangement.mediumSpacing(): Arrangement.HorizontalOrVertical = Arrangement.spacedBy(MaterialTheme.spacing.medium)

@Composable
fun Arrangement.mediumLargeSpacing(): Arrangement.HorizontalOrVertical = Arrangement.spacedBy(MaterialTheme.spacing.mediumLarge)