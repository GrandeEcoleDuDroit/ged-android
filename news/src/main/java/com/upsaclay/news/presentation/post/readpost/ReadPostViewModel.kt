package com.upsaclay.news.presentation.post.readpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.PostRepository
import com.upsaclay.news.domain.post.usecase.DeletePostUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReadPostViewModel(
    private val postId: String,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val deletePostUseCase: DeletePostUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ReadPostUiState())
    val uiState: StateFlow<ReadPostUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenPosts()
        listenUser()
    }

    fun deletePost() {
        val post = uiState.value.post ?: return
        executeRequest {
            deletePostUseCase.execute(post)
            _event.emit(ReadPostUiEvent.PostDeleted)
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
            postRepository.getLocalPostFlow(postId)
                .filterNotNull()
                .collect { post ->
                    _uiState.update {
                        it.copy(post = post)
                    }
                }
        }
    }

    private fun listenUser() {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update { state -> state.copy(user = user) }
            }
        }
    }

    data class ReadPostUiState(
        val post: Post? = null,
        val user: User? = null,
        val loading: Boolean = false
    )

    sealed interface ReadPostUiEvent : SingleUiEvent {
        data object PostDeleted : ReadPostUiEvent
    }
}