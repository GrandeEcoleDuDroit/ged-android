package com.upsaclay.news.presentation.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.unit.dp
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.ExpandableText
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.postFixture
import java.time.LocalDateTime

@Composable
fun ExtendedPostItem(
    modifier: Modifier = Modifier,
    post: Post,
    onRedirectPostClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    when (post.state) {
        is PostState.Published, PostState.Draft -> {
            DefaultItem(
                modifier = modifier,
                post = post,
                onRedirectPostClick = onRedirectPostClick,
                onOptionClick = onOptionClick
            )
        }

        is PostState.Publishing -> {
            PublishingItem(
                modifier = modifier,
                post = post,
                onRedirectPostClick = onRedirectPostClick,
                onOptionClick = onOptionClick
            )
        }

        is PostState.Error -> {
            ErrorItem(
                modifier = modifier,
                post = post,
                onRedirectPostClick = onRedirectPostClick,
                onOptionClick = onOptionClick
            )
        }
    }
}

@Composable
private fun DefaultItem(
    modifier: Modifier = Modifier,
    post: Post,
    onRedirectPostClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        TitleSection(
            title = post.title,
            onOptionClick = onOptionClick
        )

        if (post.state.imageReferenceValues.isNotEmpty()) {
            ImageSection(
                modifier = Modifier.height(dimensionResource(R.dimen.compact_post_image_height)),
                imageReferences = post.state.imageReferenceValues
            )
        }

        post.content?.let {
            ContentSection(content = it)
        }

        FooterSection(
            postSource = post.source,
            date = post.date,
            onRedirectPostClick = onRedirectPostClick
        )
    }
}

@Composable
private fun PublishingItem(
    modifier: Modifier = Modifier,
    post: Post,
    onRedirectPostClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    DefaultItem(
        modifier = modifier.alpha(0.5f),
        post = post,
        onRedirectPostClick = onRedirectPostClick,
        onOptionClick = onOptionClick
    )
}

@Composable
private fun ErrorItem(
    modifier: Modifier = Modifier,
    post: Post,
    onRedirectPostClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        Row(
            horizontalArrangement = Arrangement.smallSpacing(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            TitleSection(
                title = post.title,
                onOptionClick = onOptionClick
            )
        }

        if (post.state.imageReferenceValues.isNotEmpty()) {
            ImageSection(
                modifier = Modifier.height(dimensionResource(R.dimen.compact_post_image_height)),
                imageReferences = post.state.imageReferenceValues
            )
        }

        post.content?.let {
            ContentSection(content = it)
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
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.smallSpacing(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis
        )

        OptionButton(
            modifier = Modifier.size(32.dp),
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
    ExpandableText(
        text = content,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun FooterSection(
    modifier: Modifier = Modifier,
    postSource: Post.PostSource,
    date: LocalDateTime,
    onRedirectPostClick: () -> Unit,
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
@Composable
private fun DefaultItemPreview() {
    GedoiseTheme {
        Surface {
            DefaultItem(
                post = postFixture,
                onRedirectPostClick = {},
                onOptionClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun PublishingItemPreview() {
    GedoiseTheme {
        Surface {
            PublishingItem (
                post = postFixture,
                onRedirectPostClick = {},
                onOptionClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun ErrorItemPreview() {
    GedoiseTheme {
        Surface {
            ErrorItem(
                post = postFixture,
                onRedirectPostClick = {},
                onOptionClick = {}
            )
        }
    }
}