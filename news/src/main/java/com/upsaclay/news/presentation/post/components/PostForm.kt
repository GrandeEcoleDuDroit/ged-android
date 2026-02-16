package com.upsaclay.news.presentation.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.SimpleAsyncImage
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.imageIconButtonColors
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post.PostSource
import com.upsaclay.news.presentation.announcement.AnnouncementPresentationUtils.announcementTitleStyle
import com.upsaclay.news.presentation.post.PostPresentationUtils.postContentStyle
import com.upsaclay.news.presentation.post.PostPresentationUtils.postLinkStyle
import com.upsaclay.news.presentation.post.PostPresentationUtils.postTitleStyle

@Composable
fun PostForm(
    modifier: Modifier = Modifier,
    value: PostFormValue,
    onTitleChange: (String) -> Unit,
    onPostLinkChange: (String) -> Unit,
    onPostSourceChange: (PostSource) -> Unit,
    onContentChange: (String) -> Unit,
    onRemoveImageClick: (Int) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        InputSection(
            value = value,
            onTitleChange = onTitleChange,
            onPostLinkChange = onPostLinkChange,
            onPostSourceChange = onPostSourceChange,
            onContentChange = onContentChange
        )

        ImageRail(
            modifier = Modifier.fillMaxWidth(),
            imageReferences = value.imageReferences,
            onRemoveImageClick = onRemoveImageClick
        )
    }
}

@Composable
private fun InputSection(
    value: PostFormValue,
    onTitleChange: (String) -> Unit,
    onPostLinkChange: (String) -> Unit,
    onPostSourceChange: (PostSource) -> Unit,
    onContentChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        TransparentTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = value.title,
            onValueChange = onTitleChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.title_field_placeholder),
                    style = announcementTitleStyle
                )
            },
            textStyle = postTitleStyle
        )

        PostLinkInput(
            postLink = value.postLink,
            postLinkError = value.postLinkError,
            onPostLinkChange = onPostLinkChange
        )

        PostSourceInput(
            postSource = value.postSource,
            allPostSources = value.allPostSources,
            onPostSourceChange = onPostSourceChange
        )

        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value.content,
            onValueChange = onContentChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.content_field_placeholder),
                    style = postContentStyle
                )
            },
            textStyle = postContentStyle
        )
    }
}

@Composable
private fun PostLinkInput(
    postLink: String,
    postLinkError: String?,
    onPostLinkChange: (String) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.smallSpacing(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_outline_link_2),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.inputForeground
            )

            TransparentTextField(
                modifier = Modifier.fillMaxWidth(),
                value = postLink,
                onValueChange = onPostLinkChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.post_link_field_placeholder),
                        style = postLinkStyle
                    )
                },
                textStyle = postLinkStyle,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None
                ),
                singleLine = true
            )
        }

        postLinkError?.let {
            Text(
                modifier = Modifier.padding(
                    start = dimensionResource(com.upsaclay.common.R.dimen.medium_icon_size) +
                            dimensionResource(com.upsaclay.common.R.dimen.small_padding),
                    top = dimensionResource(com.upsaclay.common.R.dimen.supporting_text_top_padding)
                ),
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun PostSourceInput(
    postSource: PostSource?,
    allPostSources: List<PostSource>,
    onPostSourceChange: (PostSource) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.smallMediumSpacing()
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_outline_language),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.inputForeground
        )

        allPostSources.forEach {
            FilterChip(
                selected = it == postSource,
                onClick = { onPostSourceChange(it) },
                label = { Text(text = it.label) }
            )
        }
    }
}

@Composable
private fun ImageRail(
    modifier: Modifier = Modifier,
    imageReferences: List<String>,
    onRemoveImageClick: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.mediumSpacing()
    ) {
        itemsIndexed(imageReferences) { index, imageReference ->
            ImageRailItem(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.create_post_image_rail_item_height))
                    .width(dimensionResource(R.dimen.create_post_image_rail_item_width)),
                imageReference = imageReference,
                onRemoveImageClick = { onRemoveImageClick(index) }
            )
        }
    }
}

@Composable
private fun ImageRailItem(
    modifier: Modifier = Modifier,
    imageReference: String,
    onRemoveImageClick: () -> Unit
) {
    Box(
        modifier = modifier.clip(ShapeDefaults.Small),
        contentAlignment = Alignment.TopEnd
    ) {
        SimpleAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageReference
        )

        IconButton(
            modifier = Modifier
                .padding(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding))
                .size(30.dp),
            onClick = onRemoveImageClick,
            colors = MaterialTheme.colorScheme.imageIconButtonColors
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(com.upsaclay.common.R.drawable.ic_close),
                contentDescription = null
            )
        }
    }
}

data class PostFormValue(
    val title: String,
    val postLink: String,
    val postSource: PostSource?,
    val allPostSources: List<PostSource>,
    val content: String,
    val imageReferences: List<String>,
    val postLinkError: String? = null
)

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun PostFormPreview() {
    var title by remember { mutableStateOf("") }
    var postLink by remember { mutableStateOf("") }
    var postSource by remember { mutableStateOf<PostSource?>(null) }
    var content by remember { mutableStateOf("") }
    val imageReferences = listOf("", "", "")

    GedoiseTheme {
        Surface {
            PostForm(
                value = PostFormValue(
                    title = title,
                    postLink = postLink,
                    postSource = null,
                    allPostSources = PostSource.entries,
                    content = content,
                    imageReferences = imageReferences
                ),
                onTitleChange = { title = it },
                onPostLinkChange = { postLink = it },
                onPostSourceChange = { postSource = it },
                onContentChange = { content = it },
                onRemoveImageClick = {}
            )
        }
    }
}