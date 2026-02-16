package com.upsaclay.news.presentation.post.allposts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.noRippleClickable
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.ListDivider
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.postsFixture
import com.upsaclay.news.presentation.post.components.ExtendedPostItem
import com.upsaclay.news.presentation.post.components.PostBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllPostsDestination(
    onBackClick: () -> Unit,
    onEditPostClick: (Post) -> Unit,
    viewModel: AllPostsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))

                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    if (uiState.user != null) {
        AllPostsScreen(
            user = uiState.user!!,
            posts = uiState.posts,
            refreshing = uiState.refreshing,
            loading = uiState.loading,
            snackbarHostState = snackbarHostState,
            onBackClick = onBackClick,
            onRefresh = viewModel::refreshPosts,
            onRecreatePostClick = viewModel::recreatePost,
            onEditPostClick = onEditPostClick,
            onDeletePostClick = viewModel::deletePost
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllPostsScreen(
    user: User,
    posts: List<Post>?,
    refreshing: Boolean,
    loading: Boolean,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onRecreatePostClick: (Post) -> Unit,
    onEditPostClick: (Post) -> Unit,
    onDeletePostClick: (Post) -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<AllPostScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<AllPostDialog?>(null) }

    when (val dialog = activeDialog) {
        is AllPostDialog.DeletePostDialog -> {
            DefaultDialog(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
                text = stringResource(id = R.string.delete_post_dialog_message),
                confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeletePostClick(dialog.post)
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
                title = stringResource(R.string.all_news)
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        posts?.let {
            PullToRefreshComponent(
                modifier = Modifier.padding(innerPadding),
                onRefresh = onRefresh,
                refreshing = refreshing
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (posts.isEmpty()) {
                        item {
                            EmptyText(text = stringResource(id = R.string.no_news))
                        }
                    } else {
                        itemsIndexed(posts) { index, post ->
                            ListDivider()

                            ExtendedPostItem(
                                modifier = Modifier
                                    .noRippleClickable {
                                        if (post.state !is Post.PostState.Published) {
                                            activeBottomSheet = AllPostScreenBottomSheet.PostBottomSheet(post)
                                        }
                                    }
                                    .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                                post = post,
                                onOptionClick = {
                                    activeBottomSheet = AllPostScreenBottomSheet.PostBottomSheet(post)
                                }
                            )

                            if (index == posts.lastIndex) {
                                ListDivider()
                            }
                        }
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressBar(
                    modifier = Modifier.padding(top = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                )
            }
        }
    }
    when (val bottomSheet = activeBottomSheet) {
        is AllPostScreenBottomSheet.PostBottomSheet -> {
            PostBottomSheet(
                postState = bottomSheet.post.state,
                isEditable = user.admin,
                onEditClick = {
                    activeBottomSheet = null
                    onEditPostClick(bottomSheet.post)
                },
                onRecreateClick = {
                    activeBottomSheet = null
                    onRecreatePostClick(bottomSheet.post)
                },
                onDeleteClick = {
                    activeBottomSheet = null
                    activeDialog = AllPostDialog.DeletePostDialog(bottomSheet.post)
                },
                onDismiss = { activeBottomSheet = null }
            )
        }

        else -> Unit
    }
}

private sealed class AllPostScreenBottomSheet {
    data class PostBottomSheet(val post: Post) : AllPostScreenBottomSheet()
}

private sealed class AllPostDialog {
    data class DeletePostDialog(val post: Post) : AllPostDialog()
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun AllPostsScreenPreview() {
    GedoiseTheme {
        AllPostsScreen(
            user = userFixture,
            posts = postsFixture,
            refreshing = false,
            loading = false,
            onBackClick = {},
            onRefresh = {},
            onRecreatePostClick = {},
            onEditPostClick = {},
            onDeletePostClick = {}
        )
    }
}