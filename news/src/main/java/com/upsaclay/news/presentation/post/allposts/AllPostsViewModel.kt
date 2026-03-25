package com.upsaclay.news.presentation.post.allposts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.PostReport
import com.upsaclay.news.domain.post.PostRepository
import com.upsaclay.news.domain.post.usecase.DeletePostUseCase
import com.upsaclay.news.domain.post.usecase.RecreatePostUseCase
import com.upsaclay.news.domain.post.usecase.RefreshPostsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllPostsViewModel(
    private val refreshPostsUseCase: RefreshPostsUseCase,
    private val recreatePostUseCase: RecreatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
    ): ViewModel() {
    private val _uiState = MutableStateFlow(AllPostsUiState())
    val uiState: StateFlow<AllPostsUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenPosts()
        listenUser()
    }

    fun refreshPosts() {
        viewModelScope.executeUiBlockingRequest(
            block = { refreshPostsUseCase.execute() },
            onLoading = {
                _uiState.update {
                    it.copy(refreshing = true)
                }
            },
            onError = {
                _event.emit(SingleUiEvent.Error( R.string.news_refresh_error))
            },
            onFinished = {
                _uiState.update { it.copy(refreshing = false) }
            }
        )
    }

    fun recreatePost(post: Post) {
        viewModelScope.launch {
            recreatePostUseCase.execute(post)
        }
    }

    fun deletePost(post: Post) {
        executeRequest {
            deletePostUseCase.execute(post)
            _event.emit(SingleUiEvent.Success(R.string.post_deleted))
        }
    }

    fun reportPost(report: PostReport) {
        executeRequest {
            postRepository.reportPost(report)
            _event.emit(SingleUiEvent.Success(R.string.announcement_reported))
        }
    }

    private fun executeRequest(block: suspend () -> Unit) {
        viewModelScope.executeUiBlockingRequest(
            block = block,
            onLoading = {
                _uiState.update { it.copy(loading = true) }
            },
            onError = {
                _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(it)))
            },
            onFinished = {
                _uiState.update { it.copy(loading = false) }
            }
        )
    }

    private fun listenPosts() {
        viewModelScope.launch {
            postRepository.posts.collect { posts ->
                _uiState.update {
                    it.copy(posts = posts)
                }
            }
        }
    }

    private fun listenUser() {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update {
                    it.copy(user = user)
                }
            }
        }
    }

    data class AllPostsUiState(
        val posts: List<Post>? = null,
        val user: User? = null,
        val refreshing: Boolean = false,
        val loading: Boolean = false
    )
}