package com.upsaclay.news.presentation.post.createpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.usecase.CreatePostUseCase
import com.upsaclay.news.presentation.post.PostLinkError
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_CONTENT_LENGTH
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_IMAGE_COUNT
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_POST_LINK_LENGTH
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_TITLE_LENGTH
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val generateIdUseCase: GenerateIdUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private val postCreateState = MutableStateFlow(PostCreateState())

    init {
        listenPostCreateState()
    }

    fun onTitleChange(title: String) {
        val truncatedTitle = title.take(MAX_TITLE_LENGTH)
        _uiState.update {
            it.copy(title = truncatedTitle)
        }
        postCreateState.update {
            it.copy(validTitle = validateTitle(truncatedTitle))
        }
    }

    fun onPostLinkChange(postLink: String) {
        _uiState.update {
            it.copy(postLink = postLink)
        }
        postCreateState.update {
            it.copy(validPostLink = validatePostLink(postLink))
        }
    }

    fun onSelectPostSource(postSource: Post.PostSource) {
        val newPostSource = if (postSource == uiState.value.postSource) null else postSource
        _uiState.update {
            it.copy(postSource = newPostSource)
        }
        postCreateState.update {
            it.copy(validPostSource = validatePostSource(newPostSource))
        }
    }

    fun onContentChange(content: String) {
        val truncatedContent = content.take(MAX_CONTENT_LENGTH)
        _uiState.update {
            it.copy(content = truncatedContent)
        }
        postCreateState.update {
            it.copy(validContent = validateContent(truncatedContent))
        }
    }

    fun onAddImageUris(uris: List<Uri>) {
        val newImageUris = uiState.value.imageUris + uris
        if (newImageUris.size > MAX_IMAGE_COUNT) {
            viewModelScope.launch {
                _event.emit(SingleUiEvent.Error(R.string.post_max_image_count_error))
            }
            return
        }

        _uiState.update {
            it.copy(imageUris = newImageUris)
        }

        postCreateState.update {
            it.copy(validImageUris = validateImageUris(newImageUris))
        }
    }

    fun onRemoveImageUri(index: Int) {
        val currentImageUris = uiState.value.imageUris
        val newImageUris = currentImageUris - currentImageUris[index]
        _uiState.update {
            it.copy(imageUris = newImageUris)
        }
        postCreateState.update {
            it.copy(validImageUris = validateImageUris(newImageUris))
        }
    }

    fun createPost() {
        val (title, link, source, content, imageUris) = uiState.value
        if (source == null) return

        val post = Post(
            id = generateIdUseCase.execute(),
            title = title.trim(),
            content = content.trim(),
            link = link.trim(),
            source = source,
            date = LocalDateTime.now(ZoneOffset.UTC),
            state = Post.PostState.Draft
        )
        createPostUseCase.execute(post, imageUris.map { it.toString() })
    }

    private fun validateTitle(title: String): Boolean = title.isNotBlank()

    private fun validatePostLink(postLink: String): Boolean {
        _uiState.update {
            it.copy(
                postLinkError = when {
                    postLink.length > MAX_POST_LINK_LENGTH -> PostLinkError.ExceedLengthLimit
                    else -> null
                }
            )
        }

        return with(uiState.value) {
            postLinkError == null && postLink.isNotBlank()
        }
    }

    private fun validatePostSource(postSource: Post.PostSource?): Boolean = postSource != null

    private fun validateContent(content: String): Boolean = content.isNotBlank()

    private fun validateImageUris(imageUris: List<Uri>): Boolean =
        imageUris.isNotEmpty() && imageUris.size <= MAX_IMAGE_COUNT

    private fun listenPostCreateState() {
        viewModelScope.launch {
            postCreateState.collect { postCreateState ->
                _uiState.update {
                    it.copy(createEnabled = postCreateState.valid)
                }
            }
        }
    }

    data class CreatePostUiState(
        val title: String = "",
        val postLink: String = "",
        val postSource: Post.PostSource? = null,
        val content: String = "",
        val imageUris: List<Uri> = emptyList(),
        val postLinkError: PostLinkError? = null,
        val createEnabled: Boolean = false
    ) {
        val allPostSources: List<Post.PostSource> = Post.PostSource.entries
    }

    private data class PostCreateState(
        val validTitle: Boolean = false,
        val validPostLink: Boolean = false,
        val validPostSource: Boolean = false,
        val validContent: Boolean = false,
        val validImageUris: Boolean = false
    ) {
        val valid: Boolean
            get() = validTitle &&
                    validPostLink &&
                    validPostSource &&
                    (validContent || validImageUris)
    }
}