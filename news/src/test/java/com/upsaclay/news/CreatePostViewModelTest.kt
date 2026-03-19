package com.upsaclay.news

import android.net.Uri
import com.upsaclay.common.domain.entity.ByteUnit
import com.upsaclay.common.domain.entity.FileInformation
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.postFixture
import com.upsaclay.news.domain.post.usecase.CreatePostUseCase
import com.upsaclay.news.presentation.post.createpost.CreatePostViewModel
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

class CreatePostViewModelTest {
    private val imageRepository: ImageRepository = mockk()
    private val createPostUseCase: CreatePostUseCase = mockk()
    private val generateIdUseCase: GenerateIdUseCase = mockk()

    private lateinit var viewModel: CreatePostViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val title = "title"
    private val postLink = "postLink"
    private val content = "content"
    private val postSource = Post.PostSource.INSTAGRAM
    private val imageUri = mockk<Uri>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { imageRepository.getFileInformation(any()) } returns FileInformation()
        every { generateIdUseCase.execute() } returns "id"
        coEvery { createPostUseCase.execute(any(), any()) } returns Unit

        viewModel = CreatePostViewModel(
            imageRepository = imageRepository,
            createPostUseCase = createPostUseCase,
            generateIdUseCase = generateIdUseCase
        )
    }

    @Test
    fun default_values_are_correct() {
        assertEquals("", viewModel.uiState.value.title)
        assertEquals("", viewModel.uiState.value.postLink)
        assertEquals(null, viewModel.uiState.value.postSource)
        assertEquals("", viewModel.uiState.value.content)
        assertEquals(emptyList(), viewModel.uiState.value.imageUris)
        assertEquals(null, viewModel.uiState.value.postLinkError)
        assertEquals(false, viewModel.uiState.value.createEnabled)
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
        val postSource = postFixture.source

        // When
        viewModel.onSelectPostSource(postSource)
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

        // When
        viewModel.onAddImageUris(imageUris)

        // Then
        assertEquals(imageUris, viewModel.uiState.value.imageUris)
    }

    @Test
    fun onAddImageUris_should_not_add_image_with_size_superior_than_3mb() {
        // Given
        every { imageRepository.getFileInformation(any()) } returns FileInformation(size = 4 * ByteUnit.MEGA_BYTE.value)
        val imageUris = listOf(imageUri)

        // When
        viewModel.onAddImageUris(imageUris)

        // Then
        assertEquals(emptyList(), viewModel.uiState.value.imageUris)
    }

    @Test
    fun onAddImageUris_should_add_10_images_maximum() {
        // Given
        val imageUris = List(11) { imageUri }

        // When
        viewModel.onAddImageUris(imageUris)

        // Then
        assertEquals(10, viewModel.uiState.value.imageUris.size)
    }

    @Test
    fun onRemoveImageUri_should_remove_image_uri() {
        // Given
        val imageUris = listOf(imageUri, imageUri)
        val index = 0
        val expectedResult = listOf(imageUri)

        // When
        viewModel.onAddImageUris(imageUris)
        viewModel.onRemoveImageUri(index)

        // Then
        assertEquals(expectedResult, viewModel.uiState.value.imageUris)
    }

    @Test
    fun createPost_should_create_post() {
        // Given
        viewModel.onSelectPostSource(postSource)

        // When
        viewModel.createPost()

        // Then
        coVerify { createPostUseCase.execute(any(), any()) }
    }

    @Test
    fun createEnabled_should_be_true_when_all_fields_and_content_is_filled() {
        // Given
        viewModel.onTitleChange(title)
        viewModel.onPostLinkChange(postLink)
        viewModel.onSelectPostSource(postSource)
        viewModel.onContentChange(content)

        // Then
        assertEquals(true, viewModel.uiState.value.createEnabled)
    }

    @Test
    fun createEnabled_should_be_true_when_all_fields_and_image_uris_is_filled() {
        // Given
        viewModel.onTitleChange(title)
        viewModel.onPostLinkChange(postLink)
        viewModel.onSelectPostSource(postSource)
        viewModel.onAddImageUris(listOf(imageUri))

        // Then
        assertEquals(true, viewModel.uiState.value.createEnabled)
    }

    @Test
    fun updateEnabled_should_be_false_when_any_field_is_empty() {
        // Given
        viewModel.onTitleChange("")

        // Then
        assertEquals(false, viewModel.uiState.value.createEnabled)
    }
}