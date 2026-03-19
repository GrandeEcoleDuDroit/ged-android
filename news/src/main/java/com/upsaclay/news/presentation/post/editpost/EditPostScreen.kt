package com.upsaclay.news.presentation.post.editpost

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.upsaclay.common.domain.entity.ByteUnit
import com.upsaclay.common.domain.extensions.toBytes
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.ImageReference
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.presentation.post.PostImageError
import com.upsaclay.news.presentation.post.PostLinkError
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_IMAGE_COUNT
import com.upsaclay.news.presentation.post.PostPreviewParameterProvider
import com.upsaclay.news.presentation.post.components.PostForm
import com.upsaclay.news.presentation.post.components.PostFormValue
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditPostDestination(
    post: Post,
    onCancelClick: () -> Unit,
    viewModel: EditPostViewModel = koinViewModel(
        parameters = { parametersOf(post) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is EditPostViewModel.EditPostUiEvent.ImageError -> {
                    val message = when (val imageError = it.postImageError) {
                        is PostImageError.TooManyImages -> context.getString(imageError.error, imageError.LIMIT)
                        is PostImageError.ImageTooLarge -> {
                            val maxValue = context.getString(
                                com.upsaclay.common.R.string.mega_bytes_short,
                                imageError.LIMIT.toBytes(ByteUnit.MEGA_BYTE)
                            )
                            context.getString(imageError.error, maxValue)
                        }
                    }
                    showSnackBar(message)
                }
                is SingleUiEvent.Error -> showSnackBar(context.getString(it.messageId))
                is SingleUiEvent.Success -> onCancelClick()
            }
        }
    }

    EditPostScreen(
        title = uiState.title,
        postLink = uiState.postLink,
        postSource = uiState.postSource,
        allPostSources = uiState.allPostSources,
        content = uiState.content,
        imageReferences = uiState.imageReferences,
        postLinkError = uiState.postLinkError,
        loading = uiState.loading,
        updateEnabled = uiState.updateEnabled,
        snackbarHostState = snackbarHostState,
        onTitleChange = viewModel::onTitleChange,
        onPostLinkChange = viewModel::onPostLinkChange,
        onPostSourceChange = viewModel::onSelectPostSource,
        onContentChange = viewModel::onContentChange,
        onAddImageUris = viewModel::onAddImageUris,
        onRemoveImageUri = viewModel::onRemoveImageReference,
        onUpdatePostClick = viewModel::updatePost,
        onCancelClick = onCancelClick
    )
}

@Composable
private fun EditPostScreen(
    title: String,
    postLink: String,
    postSource: Post.PostSource?,
    allPostSources: List<Post.PostSource>,
    content: String,
    imageReferences: List<ImageReference>,
    postLinkError: PostLinkError?,
    loading: Boolean,
    updateEnabled: Boolean,
    snackbarHostState: SnackbarHostState,
    onTitleChange: (String) -> Unit,
    onPostLinkChange: (String) -> Unit,
    onPostSourceChange: (Post.PostSource) -> Unit,
    onContentChange: (String) -> Unit,
    onAddImageUris: (List<Uri>) -> Unit,
    onRemoveImageUri: (Int) -> Unit,
    onUpdatePostClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGE_COUNT),
        onResult = onAddImageUris
    )

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            EditTopBar(
                title = stringResource(R.string.edit_post),
                onCancelClick = {
                    focusManager.clearFocus()
                    onCancelClick()
                },
                onActionClick = {
                    focusManager.clearFocus()
                    onUpdatePostClick()
                },
                actionLabel = stringResource(com.upsaclay.common.R.string.save),
                buttonEnable = updateEnabled
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                        focusManager.clearFocus()
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.inputForeground
                    )
                ) {
                    Icon(
                        painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_image),
                        contentDescription = stringResource(com.upsaclay.common.R.string.add_image)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(data)
            }
        }
    ) { innerPadding ->
        PostForm(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.padding.medium),
            value = PostFormValue(
                title = title,
                postLink = postLink,
                postSource = postSource,
                allPostSources = allPostSources,
                content = content,
                imageReferences = imageReferences.map { it.value },
                postLinkError = when (postLinkError) {
                    is PostLinkError.LinkTooLong -> stringResource(postLinkError.error, postLinkError.LIMIT)
                    null -> null
                }
            ),
            onTitleChange = onTitleChange,
            onPostLinkChange = onPostLinkChange,
            onPostSourceChange = onPostSourceChange,
            onContentChange = onContentChange,
            onRemoveImageClick = onRemoveImageUri
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun EditPostScreenPreview(
    @PreviewParameter(PostPreviewParameterProvider::class) post: Post
) {
    var title by remember { mutableStateOf(post.title) }
    var postLink by remember { mutableStateOf(post.link) }
    var postSource by remember { mutableStateOf(post.source) }
    var content by remember { mutableStateOf(post.content ?: "") }

    GedoiseTheme {
        EditPostScreen(
            title = title,
            postLink = postLink,
            postSource = postSource,
            allPostSources = Post.PostSource.entries,
            content = content,
            imageReferences = emptyList(),
            postLinkError = null,
            loading = false,
            updateEnabled = false,
            snackbarHostState = SnackbarHostState(),
            onTitleChange = { title = it },
            onPostLinkChange = { postLink = it },
            onPostSourceChange = { postSource = it },
            onContentChange = { content = it },
            onAddImageUris = {},
            onRemoveImageUri = {},
            onCancelClick = {},
            onUpdatePostClick = {}
        )
    }
}