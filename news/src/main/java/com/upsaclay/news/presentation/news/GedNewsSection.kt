package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.SectionTitle
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.postsFixture
import com.upsaclay.news.presentation.post.components.CompactPostItem

@Composable
fun GedNewsSection(
    modifier: Modifier = Modifier,
    posts: List<Post>?
) {
    val pagerState = rememberPagerState { posts?.size ?: 0 }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        SectionTitle(
            modifier = Modifier
                .padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                .fillMaxWidth(),
            title = stringResource(id = R.string.ged_news)
        )

        posts?.let {
            if (posts.isEmpty()) {
                EmptyText(text = stringResource(R.string.no_news))
            } else {
                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 1,
                    pageSpacing = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        snapPositionalThreshold = 0.1f
                    )
                ) { page ->
                    val post = posts[page]
                    CompactPostItem(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        post = post
                    )
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressBar()
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun GedNewsSectionPreview() {
    GedoiseTheme {
        Surface {
            GedNewsSection(
                posts = postsFixture
            )
        }
    }
}