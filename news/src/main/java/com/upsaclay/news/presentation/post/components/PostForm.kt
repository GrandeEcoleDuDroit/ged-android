package com.upsaclay.news.presentation.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.SimpleAsyncImage
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.black
import com.upsaclay.common.presentation.theme.imageIconButtonColors
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.presentation.theme.white
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.presentation.post.PostPresentationUtils.contentStyle
import com.upsaclay.news.presentation.post.PostPresentationUtils.postLinkStyle
import com.upsaclay.news.presentation.post.PostPresentationUtils.titleStyle

@Composable
fun PostForm(
    modifier: Modifier = Modifier,
    value: PostFormValue,
    onTitleChange: (String) -> Unit,
    onPostLinkChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onEditImagesClick: () -> Unit,
    onRemoveImagesClick: () -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        InputSection(
            value = value,
            onTitleChange = onTitleChange,
            onPostLinkChange = onPostLinkChange,
            onContentChange = onContentChange
        )

        if (value.imageReferences.isNotEmpty()) {
            ImageSection(
                imageReferences = value.imageReferences,
                onEditImagesClick = onEditImagesClick,
                onRemoveImagesClick = onRemoveImagesClick
            )
        }
    }
}

@Composable
private fun InputSection(
    value: PostFormValue,
    onTitleChange: (String) -> Unit,
    onPostLinkChange: (String) -> Unit,
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
                    style = titleStyle
                )
            },
            textStyle = titleStyle
        )

        Row(
            horizontalArrangement = Arrangement.smallSpacing()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_outline_link_2),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.inputForeground
            )

            TransparentTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value.postLink,
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
                )
            )
        }

        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value.content,
            onValueChange = onContentChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.content_field_placeholder),
                    style = contentStyle
                )
            },
            textStyle = contentStyle
        )
    }
}

@Composable
private fun ImageSection(
    imageReferences: List<String>,
    onEditImagesClick: () -> Unit,
    onRemoveImagesClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(34.dp),
                onClick = onEditImagesClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.black.copy(alpha = 0.8f),
                    contentColor = MaterialTheme.colorScheme.white
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_outline_edit),
                    contentDescription = "Edit images"
                )
            }

            Spacer(modifier = Modifier.width(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

            IconButton(
                modifier = Modifier.size(34.dp),
                onClick = onRemoveImagesClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.black.copy(alpha = 0.8f),
                    contentColor = MaterialTheme.colorScheme.white
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(com.upsaclay.common.R.drawable.ic_close),
                    contentDescription = "Remove images"
                )
            }
        }

        ImageGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = dimensionResource(R.dimen.image_grid_height)),
            imageReferences = imageReferences
        )
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(imageReferences) { index, imageReference ->
            ImageRailItem(
                imageReference = imageReference,
                onRemoveImageClick = { onRemoveImageClick(index) }
            )
        }
    }
}

@Composable
private fun ImageRailItem(
    imageReference: String,
    onRemoveImageClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        SimpleAsyncImage(model = imageReference)

        IconButton(
            onClick = onRemoveImageClick,
            colors = MaterialTheme.colorScheme.imageIconButtonColors
        ) {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_close),
                contentDescription = null
            )
        }
    }
}

data class PostFormValue(
    val title: String,
    val postLink: String,
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
    var content by remember { mutableStateOf("") }
    val imageReferences = listOf("", "", "")

    GedoiseTheme {
        Surface {
            PostForm(
                value = PostFormValue(
                    title = title,
                    postLink = postLink,
                    content = content,
                    imageReferences = imageReferences
                ),
                onTitleChange = { title = it },
                onPostLinkChange = { postLink = it },
                onContentChange = { content = it },
                onEditImagesClick = {},
                onRemoveImagesClick = {}
            )
        }
    }
}