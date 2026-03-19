package com.upsaclay.news.presentation.post.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.SimpleAsyncImage
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.overlayContent
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.presentation.theme.white

@Composable
fun PostImagePages(
    modifier: Modifier = Modifier,
    models: List<String>
) {
    val pagerState = rememberPagerState { models.size }

    Box {
        HorizontalPager(
            modifier = modifier,
            state = pagerState,
            beyondViewportPageCount = 1,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapPositionalThreshold = 0.1f
            )
        ) { page ->
            SimpleAsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = models[page]
            )
        }

        if (models.size > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(MaterialTheme.padding.smallMedium)
            ) {
                PageNumberBadge(
                    index = pagerState.currentPage,
                    totalCount = pagerState.pageCount
                )
            }
        }
    }
}

@Composable
private fun PageNumberBadge(
    index: Int,
    totalCount: Int
) {
    Box(
        modifier = Modifier
            .clip(ShapeDefaults.Large)
            .background(MaterialTheme.colorScheme.overlayContent)
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${index + 1} / $totalCount",
            color = MaterialTheme.colorScheme.white,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PostImagePagesPreview() {
    val models = listOf("", "")

    GedoiseTheme {
        PostImagePages(models = models)
    }
}