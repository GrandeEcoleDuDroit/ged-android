package com.upsaclay.common.extension

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.bringIntoView(
    scrollState: ScrollState
): Modifier = composed {
    this
//    var scrollToPosition by remember {
//        mutableFloatStateOf(0f)
//    }
//    val coroutineScope = rememberCoroutineScope()
//    this
//        .onGloballyPositioned { coordinates ->
//            scrollToPosition = coordinates.positionInRoot().y
//        }
//        .onFocusEvent {
//            if (it.isFocused) {
//                coroutineScope.launch {
//                    scrollState.animateScrollTo(scrollToPosition.toInt())
//                }
//            }
//        }
}