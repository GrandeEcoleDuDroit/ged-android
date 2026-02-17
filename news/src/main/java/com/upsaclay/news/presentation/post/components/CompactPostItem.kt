package com.upsaclay.news.presentation.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.postFixture
import java.time.LocalDateTime

@Composable
fun CompactPostItem(
    modifier: Modifier = Modifier,
    post: Post,
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
            ImageSection(
                modifier = Modifier.height(dimensionResource(R.dimen.compact_post_image_height)),
                imageReferences = post.state.imageReferenceValues
            )
        }

        ContentSection(
            modifier = Modifier.weight(1f, fill = false),
            content = post.content
        )

        FooterSection(
            postSource = post.source,
            date = post.date
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
            .heightIn(max = dimensionResource(R.dimen.compact_post_image_height))
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
    date: LocalDateTime
) {
    PostSourceItem(
        modifier = modifier,
        postSource = postSource,
        date = date
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun CompactPostItemPreview() {
    GedoiseTheme {
        Surface {
            CompactPostItem(
                post = postFixture,
                onOptionClick = {}
            )
        }
    }
}