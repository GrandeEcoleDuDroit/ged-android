package com.upsaclay.news.presentation.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.common.utils.TabletPreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.postFixture
import java.time.LocalDateTime

@Composable
fun CompactPostItem(
    modifier: Modifier = Modifier,
    post: Post,
    onRedirectPostClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    val alpha = if (post.state is PostState.Publishing) 0.5f else 1f

    Column(
        modifier = modifier.alpha(alpha),
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        TitleSection(
            title = post.title,
            state = post.state,
            onOptionClick = onOptionClick
        )

        if (post.state.imageReferenceValues.isNotEmpty()) {
            ImageSection(imageReferences = post.state.imageReferenceValues)
        }

        post.content?.let {
            ContentSection(
                modifier = Modifier.weight(1f, fill = false),
                content = it
            )
        }

        FooterSection(
            postSource = post.source,
            date = post.date,
            onRedirectPostClick = onRedirectPostClick
        )
    }
}

@Composable
private fun TitleSection(
    modifier: Modifier = Modifier,
    title: String,
    state: PostState,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.smallSpacing(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state is PostState.Error) {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        OptionButton(
            modifier = Modifier.size(dimensionResource(com.upsaclay.common.R.dimen.extra_small_button_size)),
            onClick = onOptionClick,
            contentDescription = stringResource(id = R.string.post_option_icon_description)
        )
    }
}

@Composable
private fun ImageSection(
    modifier: Modifier = Modifier,
    imageReferences: List<String>
) {
    PostImagePages(
        modifier = modifier
            .height(dimensionResource(R.dimen.compact_post_image_height))
            .clip(ShapeDefaults.Medium),
        models = imageReferences
    )
}

@Composable
private fun ContentSection(
    modifier: Modifier = Modifier,
    content: String
) {
    Text(
        modifier = modifier,
        text = content,
        style = MaterialTheme.typography.bodySmall,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun FooterSection(
    modifier: Modifier = Modifier,
    postSource: Post.PostSource,
    date: LocalDateTime,
    onRedirectPostClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PostSourceItem(
            postSource = postSource,
            date = date
        )

        OutlinedButton(
            modifier = Modifier.height(dimensionResource(R.dimen.post_item_redirect_button_height)),
            contentPadding = PaddingValues(
                start = ButtonDefaults.ContentPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = ButtonDefaults.ContentPadding.calculateEndPadding(LayoutDirection.Ltr)
            ),
            onClick = onRedirectPostClick
        ) {
            Text(text = stringResource(com.upsaclay.common.R.string.see))
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@TabletPreviews
@Composable
private fun CompactPostItemPreview() {
    GedoiseTheme {
        Surface {
            CompactPostItem(
                post = postFixture,
                onRedirectPostClick = {},
                onOptionClick = {}
            )
        }
    }
}