package com.upsaclay.news

import android.net.Uri
import com.upsaclay.common.domain.entity.ByteUnit
import com.upsaclay.common.domain.entity.FileInformation
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.ImageReference
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.postFixture
import com.upsaclay.news.domain.post.usecase.UpdatePostUseCase
import com.upsaclay.news.presentation.post.editpost.EditPostViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class EditPostViewModelTest {
    private val post = postFixture
    private val imageRepository: ImageRepository = mockk()
    private val updatePostUseCase: UpdatePostUseCase = mockk()

    private lateinit var viewModel: EditPostViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val title = "title"
    private val postLink = "postLink"
    private val content = "content"
    private val imageUri = mockk<Uri>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { imageRepository.getFileInformation(any()) } returns FileInformation()
        coEvery { updatePostUseCase.execute(any(), any()) } returns Unit

        viewModel = EditPostViewModel(
            post = post,
            imageRepository = imageRepository,
            updatePostUseCase = updatePostUseCase
        )
    }

    @Test
    fun default_values_are_correct() {
        assertEquals(post.title, viewModel.uiState.value.title)
        assertEquals(post.link, viewModel.uiState.value.postLink)
        assertEquals(post.source, viewModel.uiState.value.postSource)
        assertEquals(post.content, viewModel.uiState.value.content)
        assertEquals(post.state.imageReferenceValues, viewModel.uiState.value.imageReferences.map { it.value })
        assertEquals(null, viewModel.uiState.value.postLinkError)
        assertEquals(false, viewModel.uiState.value.loading)
        assertEquals(false, viewModel.uiState.value.updateEnabled)
        assertEquals(Post.PostSource.entries, viewModel.uiState.value.allPostSources)
    }

    @Test
    fun onTitleChange_should_update_title() {
        // When
        viewModel.onTitleChange(title)

        // Then
        assertEquals(title, viewModel.uiState.value.title)
    }

    @Test
    fun onPostLinkChange_should_update_post_link() {
        // When
        viewModel.onPostLinkChange(postLink)

        // Then
        assertEquals(postLink, viewModel.uiState.value.postLink)
    }

    @Test
    fun onSelectPostSource_should_set_post_source_when_not_selected() {
        // Given
        val postSource = Post.PostSource.LINKEDIN

        // When
        viewModel.onSelectPostSource(postSource)

        // Then
        assertEquals(postSource, viewModel.uiState.value.postSource)
    }

    @Test
    fun onSelectPostSource_should_remove_post_source_when_already_selected() {
        // Given
        val postSource = post.source

        // When
        viewModel.onSelectPostSource(postSource)

        // Then
        assertEquals(null, viewModel.uiState.value.postSource)
    }

    @Test
    fun onContentChange_should_update_content() {
        // When
        viewModel.onContentChange(content)

        // Then
        assertEquals(content, viewModel.uiState.value.content)
    }

    @Test
    fun onAddImageUris_should_update_image_uris() {
        // Given
        val imageUris = listOf(imageUri)
        val expectedResult = viewModel.uiState.value.imageReferences + imageUris.map { ImageReference.ImageUri(it.toString()) }

        // When
        viewModel.onAddImageUris(imageUris)

        // Then
        assertEquals(expectedResult, viewModel.uiState.value.imageReferences)
    }

    @Test
    fun onAddImageUris_should_not_add_image_with_size_superior_than_3mb() {
        // Given
        every { imageRepository.getFileInformation(any()) } returns FileInformation(size = 4 * ByteUnit.MEGA_BYTE.value)
        val imageUris = listOf(imageUri)
        val expectedResult = viewModel.uiState.value.imageReferences

        // When
        viewModel.onAddImageUris(imageUris)

        // Then
        assertEquals(expectedResult, viewModel.uiState.value.imageReferences)
    }

    @Test
    fun onAddImageUris_should_add_10_images_maximum() {
        // Given
        val imageUris = List(11) { imageUri }

        // When
        viewModel.onAddImageUris(imageUris)

        // Then
        assertEquals(10, viewModel.uiState.value.imageReferences.size)
    }

    @Test
    fun onRemoveImageReference_should_remove_image_reference() {
        // Given
        val index = 0
        val expectedResult = viewModel.uiState.value.imageReferences - viewModel.uiState.value.imageReferences[index]

        // When
        viewModel.onRemoveImageReference(index)

        // Then
        assertEquals(expectedResult, viewModel.uiState.value.imageReferences)
    }

    @Test
    fun updatePost_should_update_post() {
        // When
        viewModel.updatePost()

        // Then
        coVerify { updatePostUseCase.execute(any(), any()) }
    }

    @Test
    fun updateEnabled_should_be_true_when_all_fields_and_content_is_filled() {
        // Given
        viewModel.onTitleChange(title)

        // Then
        assertEquals(true, viewModel.uiState.value.updateEnabled)
    }

    @Test
    fun updateEnabled_should_be_true_when_all_fields_and_image_uris_is_filled() {
        // Given
        viewModel.onTitleChange(title)

        // Then
        assertEquals(true, viewModel.uiState.value.updateEnabled)
    }

    @Test
    fun updateEnabled_should_be_false_when_any_field_is_empty() {
        // Given
        viewModel.onTitleChange("")

        // Then
        assertEquals(false, viewModel.uiState.value.updateEnabled)
    }

    @Test
    fun updateEnabled_should_be_false_when_field_is_same_as_before() {
        // Given
        viewModel.onTitleChange(post.title)

        // Then
        assertEquals(false, viewModel.uiState.value.updateEnabled)
    }
}