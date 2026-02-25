package com.upsaclay.news.presentation.post.editpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.ImageReference
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.usecase.UpdatePostUseCase
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

class EditPostViewModel(
    private val post: Post,
    private val updatePostUseCase: UpdatePostUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(
        EditPostUiState(
            title = post.title,
            postLink = post.link,
            postSource = post.source,
            content = post.content ?: "",
            imageReferences = post.state.imageReferenceValues.map(ImageReference::ImageUrl)
        )
    )
    val uiState: StateFlow<EditPostUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private val postUpdateState = MutableStateFlow(PostUpdateState())

    init {
        listenPostUpdateState()
    }

    fun updatePost() {
        val (title, link, source, content, imageReferences) = uiState.value
        if (source == null) return

        val newPost = post.copy(
            title = title.trim(),
            content = if (content.isBlank()) null else content.trim(),
            link = link.trim(),
            source = source
        )

        executeRequest {
            updatePostUseCase.execute(newPost, imageReferences)
            _event.emit(SingleUiEvent.Success())
        }
    }

    fun onTitleChange(title: String) {
        val truncatedTitle = title.take(MAX_TITLE_LENGTH)
        _uiState.update {
            it.copy(title = truncatedTitle)
        }
        postUpdateState.update {
            it.copy(
                titleUpdated = validateTitleUpdate(truncatedTitle),
                validTitle = validateTitle(truncatedTitle)
            )
        }
    }

    fun onPostLinkChange(postLink: String) {
        _uiState.update {
            it.copy(postLink = postLink)
        }
        postUpdateState.update {
            it.copy(
                postLinkUpdated = validatePostLinkUpdate(postLink),
                validPostLink = validatePostLink(postLink)
            )
        }
    }

    fun onSelectPostSource(postSource: Post.PostSource) {
        val newPostSource = if (postSource == uiState.value.postSource) null else postSource
        _uiState.update {
            it.copy(postSource = newPostSource)
        }
        postUpdateState.update {
            it.copy(
                postSourceUpdated = validatePostSourceUpdate(newPostSource),
                validPostSource = validatePostSource(newPostSource)
            )
        }
    }

    fun onContentChange(content: String) {
        val truncatedContent = content.take(MAX_CONTENT_LENGTH)
        _uiState.update {
            it.copy(content = truncatedContent)
        }
        postUpdateState.update {
            it.copy(
                contentUpdated = validateContentUpdate(truncatedContent),
                validContent = validateContent(truncatedContent)
            )
        }
    }

    fun onAddImageUris(uris: List<Uri>) {
        val references = uris.map { ImageReference.ImageUri(it.toString()) }
        val newImageReferences = uiState.value.imageReferences + references
        if (newImageReferences.size > MAX_IMAGE_COUNT) {
            viewModelScope.launch {
                _event.emit(SingleUiEvent.Error(R.string.post_max_image_count_error))
            }
            return
        }

        _uiState.update {
            it.copy(imageReferences = newImageReferences)
        }

        postUpdateState.update {
            it.copy(
                imageReferencesUpdated = validateImageReferencesUpdate(newImageReferences),
                validImageReferences = validateImageReferences(newImageReferences)
            )
        }
    }

    fun onRemoveImageReference(index: Int) {
        val currentImageReferences = uiState.value.imageReferences
        val newImageReferences = currentImageReferences - currentImageReferences[index]
        _uiState.update {
            it.copy(imageReferences = newImageReferences)
        }
        postUpdateState.update {
            it.copy(
                imageReferencesUpdated = validateImageReferencesUpdate(newImageReferences),
                validImageReferences = validateImageReferences(newImageReferences)
            )
        }
    }

    private fun validateTitleUpdate(title: String): Boolean = title != post.title

    private fun validateTitle(title: String): Boolean = title.isNotBlank()

    private fun validatePostLinkUpdate(postLink: String): Boolean = postLink != post.link

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

    private fun validatePostSourceUpdate(postSource: Post.PostSource?): Boolean = postSource != post.source

    private fun validatePostSource(postSource: Post.PostSource?): Boolean = postSource != null

    private fun validateContentUpdate(content: String): Boolean = content != post.content

    private fun validateContent(content: String): Boolean = content.isNotBlank()

    private fun validateImageReferencesUpdate(imageReferences: List<ImageReference>): Boolean =
        imageReferences != post.state.imageReferenceValues

    private fun validateImageReferences(imageReferences: List<ImageReference>): Boolean =
        imageReferences.isNotEmpty() && imageReferences.size <= MAX_IMAGE_COUNT

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

    private fun listenPostUpdateState() {
        viewModelScope.launch {
            postUpdateState.collect { postUpdateState ->
                _uiState.update {
                    it.copy(updateEnabled = postUpdateState.updated && postUpdateState.valid)
                }
            }
        }
    }

    data class EditPostUiState(
        val title: String = "",
        val postLink: String = "",
        val postSource: Post.PostSource? = null,
        val content: String = "",
        val imageReferences: List<ImageReference> = emptyList(),
        val postLinkError: PostLinkError? = null,
        val loading: Boolean = false,
        val updateEnabled: Boolean = false
    ) {
        val allPostSources: List<Post.PostSource> = Post.PostSource.entries
    }

    private data class PostUpdateState(
        val titleUpdated: Boolean = false,
        val postLinkUpdated: Boolean = false,
        val postSourceUpdated: Boolean = false,
        val contentUpdated: Boolean = false,
        val imageReferencesUpdated: Boolean = false,

        val validTitle: Boolean = true,
        val validPostLink: Boolean = true,
        val validPostSource: Boolean = true,
        val validContent: Boolean = true,
        val validImageReferences: Boolean = true,
    ) {
        val updated: Boolean
            get() = titleUpdated ||
                    postLinkUpdated ||
                    postSourceUpdated ||
                    contentUpdated ||
                    imageReferencesUpdated

        val valid: Boolean
            get() = validTitle &&
                    validPostLink &&
                    validPostSource &&
                    (validContent || validImageReferences)
    }
}