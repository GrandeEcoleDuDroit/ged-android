package com.upsaclay.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OverlayLoadingScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GedoiseColor.LittleTransparentWhite)
            .zIndex(1000f)
            .pointerInteropFilter { true }
    ){
        CircularProgressBar(
            modifier = Modifier
                .align(Alignment.Center)
                .size(70.dp)
        )
    }
}

@Composable
@Preview
private fun LoadingScreenPreview(){
    GedoiseTheme {
        OverlayLoadingScreen()
    }
}