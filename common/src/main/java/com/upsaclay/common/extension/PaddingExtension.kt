package com.upsaclay.common.extension

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource

@Composable
fun Modifier.mediumPadding(topPadding: PaddingValues): Modifier {
    return this.padding(
        top = topPadding.calculateTopPadding(),
        start = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
        end = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
        bottom = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)
    )
}

@Composable
fun Modifier.mediumPadding(): Modifier {
    return this.padding(
        start = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
        end = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
        bottom = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)
    )
}