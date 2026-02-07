package com.upsaclay.news.presentation.post.createpost

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.presentation.post.components.PostForm
import com.upsaclay.news.presentation.post.components.PostFormValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePostDestination(
    onCancelClick: () -> Unit,
    viewModel: CreatePostViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CreatePostScreen(
        title = uiState.title,
        postLink = uiState.postLink,
        content = uiState.content,
        imageUris = uiState.imageUris,
        postLinkError = uiState.postLinkError,
        createEnabled = uiState.createEnabled,
        onTitleChange = viewModel::onTitleChange,
        onPostLinkChange = viewModel::onPostLinkChange,
        onContentChange = viewModel::onContentChange,
        onAddImageUris = viewModel::onAddImageUris,
        onRemoveImageUri = viewModel::onRemoveImageUri,
        onRemoveImageUris = viewModel::onRemoveImageUris,
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
    content: String,
    imageUris: List<Uri>,
    postLinkError: CreatePostViewModel.PostLinkError?,
    createEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onPostLinkChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onAddImageUris: (List<Uri>) -> Unit,
    onRemoveImageUri: (Int) -> Unit,
    onRemoveImageUris: () -> Unit,
    onCancelClick: () -> Unit,
    onCreatePostClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = onAddImageUris
    )

    Scaffold(
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
            Row(modifier = Modifier.fillMaxWidth()) {
               IconButton(
                   onClick = {
                       multiplePhotoPickerLauncher.launch(
                           PickVisualMediaRequest(
                               ActivityResultContracts.PickVisualMedia.ImageOnly
                           )
                       )
                   },
                   colors = IconButtonDefaults.iconButtonColors(
                       contentColor = MaterialTheme.colorScheme.inputForeground
                   )
               ) {
                   Icon(
                       painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_image),
                       contentDescription = "Add image"
                   )
               }
            }
        }
    ) { innerPadding ->
        PostForm(
            modifier = Modifier.rootMediumPadding(innerPadding),
            value = PostFormValue(
                title = title,
                postLink = postLink,
                content = content,
                imageReferences = imageUris.map { it.toString() },
                postLinkError = when (postLinkError) {
                    is CreatePostViewModel.PostLinkError.ExceedLengthLimit ->
                        stringResource(postLinkError.error, postLinkError.limit, postLinkError.length)
                    null -> null
                }
            ),
            onTitleChange = onTitleChange,
            onPostLinkChange = onPostLinkChange,
            onContentChange = onContentChange,
            onEditImagesClick = {

            },
            onRemoveImagesClick = onRemoveImageUris
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
    var content by remember { mutableStateOf("") }

    GedoiseTheme {
        CreatePostScreen(
            title = title,
            postLink = postLink,
            content = content,
            imageUris = emptyList(),
            postLinkError = null,
            createEnabled = false,
            onTitleChange = { title = it },
            onPostLinkChange = { postLink = it },
            onContentChange = { content = it },
            onAddImageUris = {},
            onRemoveImageUri = {},
            onRemoveImageUris = {},
            onCancelClick = {},
            onCreatePostClick = {}
        )
    }
}