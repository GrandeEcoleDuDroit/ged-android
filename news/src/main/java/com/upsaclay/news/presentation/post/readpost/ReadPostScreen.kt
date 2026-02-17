package com.upsaclay.news.presentation.post.readpost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.postFixture
import com.upsaclay.news.presentation.post.PostPresentationUtils.postContentStyle
import com.upsaclay.news.presentation.post.PostPresentationUtils.postTitleStyle
import com.upsaclay.news.presentation.post.SizeTokens
import com.upsaclay.news.presentation.post.components.PostBottomSheet
import com.upsaclay.news.presentation.post.components.PostImagePages
import com.upsaclay.news.presentation.post.components.PostSourceItem
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReadPostDestination(
    postId: String,
    onBackClick: () -> Unit,
    onEditPostClick: (Post) -> Unit,
    viewModel: ReadPostViewModel = koinViewModel(
        parameters = { parametersOf(postId) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar: suspend (String) -> SnackbarResult = { message ->
        snackbarHostState.showSnackbar(message)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ReadPostViewModel.ReadPostUiEvent.PostDeleted -> onBackClick()
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    if (uiState.user != null && uiState.post != null) {
        ReadPostScreen(
            user = uiState.user!!,
            post = uiState.post!!,
            loading = uiState.loading,
            snackbarHostState = snackbarHostState,
            onBackClick = onBackClick,
            onEditPostClick = onEditPostClick,
            onDeletePostClick = viewModel::deletePost
        )
    } else {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressBar(
                modifier = Modifier.padding(top = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReadPostScreen(
    user: User,
    post: Post,
    loading: Boolean,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onEditPostClick: (Post) -> Unit,
    onDeletePostClick: () -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<ReadPostScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<ReadPostDialog?>(null) }

    when (activeDialog) {
        is ReadPostDialog.DeletePostDialog -> {
            DefaultDialog(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
                text = stringResource(id = R.string.delete_post_dialog_message),
                confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeletePostClick()
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
    }

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.news),
                leadingIcon = {
                    OptionButton { activeBottomSheet = ReadPostScreenBottomSheet.PostBottomSheet }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                text = post.title,
                style = postTitleStyle
            )

            PostSourceItem(
                modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                postSource = post.source,
                date = post.date,
                size = SizeTokens.MEDIUM
            )

            if (post.state.imageReferenceValues.isNotEmpty()) {
                PostImagePages(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.post_image_height)),
                    models = post.state.imageReferenceValues
                )
            }

            Text(
                modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                text = post.content,
                style = postContentStyle
            )
        }
    }

    when (activeBottomSheet) {
        is ReadPostScreenBottomSheet.PostBottomSheet -> {
            PostBottomSheet(
                postState = post.state,
                isEditable = user.admin,
                onEditClick = {
                    activeBottomSheet = null
                    onEditPostClick(post)
                },
                onDeleteClick = {
                    activeBottomSheet = null
                    activeDialog = ReadPostDialog.DeletePostDialog
                },
                onDismiss = { activeBottomSheet = null }
            )
        }

        else -> Unit
    }
}

private sealed class ReadPostScreenBottomSheet {
    data object PostBottomSheet : ReadPostScreenBottomSheet()
}

private sealed class ReadPostDialog {
    data object DeletePostDialog : ReadPostDialog()
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun ReadPostScreenPreview() {
    GedoiseTheme {
        Surface {
            ReadPostScreen(
                user = userFixture,
                post = postFixture,
                loading = false,
                snackbarHostState = SnackbarHostState(),
                onBackClick = {},
                onEditPostClick = {},
                onDeletePostClick = {}
            )
        }
    }
}