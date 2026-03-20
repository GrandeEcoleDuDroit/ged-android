package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import com.upsaclay.common.extension.noRippleClickable
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.SectionTitle
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.postsFixture
import com.upsaclay.news.presentation.post.components.CompactPostItem

@Composable
fun PostSection(
    modifier: Modifier = Modifier,
    posts: List<Post>?,
    onPostClick: (String) -> Unit,
    onUncreatedPostClick: (Post) -> Unit,
    onRedirectPostClick: (String) -> Unit,
    onPostOptionClick: (Post) -> Unit,
    onSeeAllPostsClick: () -> Unit
) {
    val pagerState = rememberPagerState { posts?.size ?: 0 }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.padding.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SectionTitle(title = stringResource(id = R.string.posts))

            TextButton(
                modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.extra_small_button_size)),
                contentPadding = PaddingValues(
                    start = ButtonDefaults.TextButtonContentPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = ButtonDefaults.TextButtonContentPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
                onClick = onSeeAllPostsClick
            ) {
                Text(text = stringResource(com.upsaclay.common.R.string.see_all))
            }
        }

        posts?.let {
            if (posts.isEmpty()) {
                EmptyText(text = stringResource(R.string.no_post))
            } else {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                    beyondViewportPageCount = 1,
                    pageSpacing = MaterialTheme.padding.medium,
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        snapPositionalThreshold = 0.1f
                    )
                ) { page ->
                    val post = posts[page]
                    CompactPostItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable {
                                if (post.state is PostState.Published) {
                                    onPostClick(post.id)
                                } else {
                                    onUncreatedPostClick(post)
                                }
                            }
                            .padding(horizontal = MaterialTheme.padding.medium),
                        post = post,
                        onRedirectPostClick = { onRedirectPostClick(post.link) },
                        onOptionClick = { onPostOptionClick(post) }
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

@PhonePreviews
@Composable
private fun PostSectionPreview() {
    GedoiseTheme {
        Surface {
            PostSection(
                posts = postsFixture,
                onPostClick = {},
                onSeeAllPostsClick = {},
                onUncreatedPostClick = {},
                onRedirectPostClick = {},
                onPostOptionClick = {}
            )
        }
    }
}