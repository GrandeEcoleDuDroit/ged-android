package com.upsaclay.news.presentation.post.createpost

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.upsaclay.news.R
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_CONTENT_LENGTH
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_POST_LINK_LENGTH
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_TITLE_LENGTH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CreatePostViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(
                title = title.take(MAX_TITLE_LENGTH),
                createEnabled = validateCreate(title = title)
            )
        }
    }

    fun onPostLinkChange(postLink: String) {
        val postLinkError = validatePostLink(postLink)
        _uiState.update {
            it.copy(
                postLink = postLink,
                postLinkError = postLinkError,
                createEnabled = validateCreate(postLink = postLink) && postLinkError == null
            )
        }
    }

    fun onContentChange(content: String) {
        _uiState.update {
            it.copy(
                content = content.take(MAX_CONTENT_LENGTH),
                createEnabled = validateCreate(content = content)
            )
        }
    }

    fun onAddImageUris(uris: List<Uri>) {
        _uiState.update {
            it.copy(imageUris = it.imageUris + uris)
        }
    }

    fun onRemoveImageUri(index: Int) {
        _uiState.update {
            it.copy(imageUris = it.imageUris - it.imageUris[index])
        }
    }

    fun onRemoveImageUris() {
        _uiState.update {
            it.copy(imageUris = emptyList())
        }
    }

    fun createPost() {

    }

    private fun validateCreate(
        title: String = uiState.value.title,
        postLink: String = uiState.value.postLink,
        content: String = uiState.value.content
    ): Boolean =
        title.isNotBlank() &&
                postLink.isNotBlank() &&
                content.isNotBlank()

    private fun validatePostLink(postLink: String): PostLinkError? {
        return if (postLink.length > MAX_POST_LINK_LENGTH) {
            PostLinkError.ExceedLengthLimit(length = postLink.length)
        } else {
            null
        }
    }

    data class CreatePostUiState(
        val title: String = "",
        val postLink: String = "",
        val content: String = "",
        val imageUris: List<Uri> = emptyList(),
        val postLinkError: PostLinkError? = null,
        val createEnabled: Boolean = false,
    )

    sealed class PostLinkError(@StringRes val error: Int) {
        data class ExceedLengthLimit(
            val limit: Int = MAX_POST_LINK_LENGTH,
            val length: Int
        ): PostLinkError(error = R.string.post_link_length_error)
    }
}