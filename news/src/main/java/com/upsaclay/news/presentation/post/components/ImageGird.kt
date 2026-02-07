package com.upsaclay.news.presentation.post.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.SimpleAsyncImage
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.overlayContent
import com.upsaclay.common.presentation.theme.white

@Composable
fun ImageGrid(
    modifier: Modifier = Modifier,
    imageReferences: List<String>
) {
    when (imageReferences.size) {
        0 -> Unit
        1 -> SingleImageGrid(modifier = modifier, imageReference = imageReferences.first())
        2 -> TwoImagesGrid(modifier = modifier, imageReferences = imageReferences)
        3 -> ThreeImagesGrid(modifier = modifier, imageReferences = imageReferences)
        4 -> FourImagesGrid(modifier = modifier, imageReferences = imageReferences)
        else -> MoreThanFourImagesGrid(modifier = modifier, imageReferences = imageReferences)
    }
}

@Composable
private fun SingleImageGrid(
    modifier: Modifier = Modifier,
    imageReference: String
) {
    SimpleAsyncImage(
        modifier = modifier,
        model = imageReference
    )
}

@Composable
private fun TwoImagesGrid(
    modifier: Modifier = Modifier,
    imageReferences: List<String>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        SimpleAsyncImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            model = imageReferences[0]
        )

        SimpleAsyncImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            model = imageReferences[1]
        )
    }
}

@Composable
private fun ThreeImagesGrid(
    modifier: Modifier = Modifier,
    imageReferences: List<String>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        SimpleAsyncImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            model = imageReferences[0]
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            SimpleAsyncImage(
                modifier = modifier.weight(1f),
                model = imageReferences[1]
            )

            SimpleAsyncImage(
                modifier = modifier.weight(1f),
                model = imageReferences[2]
            )
        }
    }
}

@Composable
private fun FourImagesGrid(
    modifier: Modifier = Modifier,
    imageReferences: List<String>
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        SimpleAsyncImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            model = imageReferences[0]
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            SimpleAsyncImage(
                modifier = modifier.weight(1f),
                model = imageReferences[1]
            )

            SimpleAsyncImage(
                modifier = modifier.weight(1f),
                model = imageReferences[2]
            )

            SimpleAsyncImage(
                modifier = modifier.weight(1f),
                model = imageReferences[3]
            )
        }
    }
}

@Composable
private fun MoreThanFourImagesGrid(
    modifier: Modifier = Modifier,
    imageReferences: List<String>
) {
    val displayImageCount = 4

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        SimpleAsyncImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            model = imageReferences[0]
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            SimpleAsyncImage(
                modifier = modifier.weight(1f),
                model = imageReferences.last()
            )

            SimpleAsyncImage(
                modifier = modifier.weight(1f),
                model = imageReferences[imageReferences.lastIndex - 1]
            )

            Box(
                modifier = modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                SimpleAsyncImage(model = imageReferences[imageReferences.lastIndex - 2])

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.overlayContent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+${imageReferences.size - displayImageCount}",
                        color = MaterialTheme.colorScheme.white
                    )
                }
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun ImageGridPreview() {
    GedoiseTheme {
        ImageGrid(
            modifier = Modifier.height(300.dp),
            imageReferences = listOf("", "", "", "", "")
        )
    }
}