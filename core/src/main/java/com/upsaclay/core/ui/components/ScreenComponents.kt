package com.upsaclay.core.ui.components

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
import com.upsaclay.core.ui.theme.GedoiseColor
import com.upsaclay.core.ui.theme.GedoiseTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadingScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GedoiseColor.LittleTransparentWhite)
            .zIndex(1f)
            .pointerInteropFilter { true }
    ){
        InfiniteCircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(70.dp)
        )
    }
}

@Composable
@Preview
fun LoadingScreenPreview(){
    GedoiseTheme {
        LoadingScreen()
    }
}