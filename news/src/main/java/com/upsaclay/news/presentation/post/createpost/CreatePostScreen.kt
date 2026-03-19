package com.upsaclay.news.presentation.post.createpost

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.ByteUnit
import com.upsaclay.common.domain.extensions.toBytes
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.presentation.post.PostImageError
import com.upsaclay.news.presentation.post.PostLinkError
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_IMAGE_COUNT
import com.upsaclay.news.presentation.post.components.PostForm
import com.upsaclay.news.presentation.post.components.PostFormValue
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePostDestination(
    onCancelClick: () -> Unit,
    viewModel: CreatePostViewModel = koinViewModel()
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
                is CreatePostViewModel.CreatePostUiEvent.ImageError -> {
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

    CreatePostScreen(
        title = uiState.title,
        postLink = uiState.postLink,
        postSource = uiState.postSource,
        allPostSources = uiState.allPostSources,
        content = uiState.content,
        imageUris = uiState.imageUris,
        postLinkError = uiState.postLinkError,
        createEnabled = uiState.createEnabled,
        snackbarHostState = snackbarHostState,
        onTitleChange = viewModel::onTitleChange,
        onPostLinkChange = viewModel::onPostLinkChange,
        onPostSourceChange = viewModel::onSelectPostSource,
        onContentChange = viewModel::onContentChange,
        onAddImageUris = viewModel::onAddImageUris,
        onRemoveImageUri = viewModel::onRemoveImageUri,
        onCancelClick = onCancelClick,
        onCreatePostClick = {
            viewModel.createPost()
            onCancelClick()
        }
    )
}

@Composable
private fun CreatePostScreen(
    title: String,
    postLink: String,
    postSource: Post.PostSource?,
    allPostSources: List<Post.PostSource>,
    content: String,
    imageUris: List<Uri>,
    postLinkError: PostLinkError?,
    createEnabled: Boolean,
    snackbarHostState: SnackbarHostState,
    onTitleChange: (String) -> Unit,
    onPostLinkChange: (String) -> Unit,
    onPostSourceChange: (Post.PostSource) -> Unit,
    onContentChange: (String) -> Unit,
    onAddImageUris: (List<Uri>) -> Unit,
    onRemoveImageUri: (Int) -> Unit,
    onCancelClick: () -> Unit,
    onCreatePostClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_IMAGE_COUNT),
        onResult = onAddImageUris
    )

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            EditTopBar(
                title = stringResource(R.string.new_post),
                onCancelClick = {
                    focusManager.clearFocus()
                    onCancelClick()
                },
                onActionClick = {
                    focusManager.clearFocus()
                    onCreatePostClick()
                },
                actionLabel = stringResource(com.upsaclay.common.R.string.publish),
                buttonEnable = createEnabled
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
                .padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            value = PostFormValue(
                title = title,
                postLink = postLink,
                postSource = postSource,
                allPostSources = allPostSources,
                content = content,
                imageReferences = imageUris.map { it.toString() },
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
private fun CreatePostScreenPreview() {
    var title by remember { mutableStateOf("") }
    var postLink by remember { mutableStateOf("") }
    var postSource by remember { mutableStateOf<Post.PostSource?>(null) }
    var content by remember { mutableStateOf("") }

    GedoiseTheme {
        CreatePostScreen(
            title = title,
            postLink = postLink,
            postSource = postSource,
            allPostSources = Post.PostSource.entries,
            content = content,
            imageUris = emptyList(),
            postLinkError = null,
            createEnabled = false,
            snackbarHostState = SnackbarHostState(),
            onTitleChange = { title = it },
            onPostLinkChange = { postLink = it },
            onPostSourceChange = { postSource = it },
            onContentChange = { content = it },
            onAddImageUris = {},
            onRemoveImageUri = {},
            onCancelClick = {},
            onCreatePostClick = {}
        )
    }
}