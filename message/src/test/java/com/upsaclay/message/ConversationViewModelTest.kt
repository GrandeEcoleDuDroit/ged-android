package com.upsaclay.message

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.domain.usecase.GetConversationsUiUseCase
import com.upsaclay.message.domain.usecase.RecreateConversationUseCase
import com.upsaclay.message.presentation.conversation.ConversationViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConversationViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val getConversationUiUseCase: GetConversationsUiUseCase = mockk()
    private val deleteConversationUseCase: DeleteConversationUseCase = mockk()
    private val recreateConversationUseCase: RecreateConversationUseCase = mockk()

    private lateinit var conversationViewModel: ConversationViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.currentUser } returns userFixture
        every { getConversationUiUseCase.execute() } returns emptyFlow()
        coEvery { recreateConversationUseCase.execute(any(), any()) } returns Unit
        coEvery { deleteConversationUseCase.execute(any(), any()) } returns Unit

        conversationViewModel = ConversationViewModel(
            userRepository = userRepository,
            getConversationsUiUseCase = getConversationUiUseCase,
            deleteConversationUseCase = deleteConversationUseCase,
            recreateConversationUseCase = recreateConversationUseCase
        )
    }

    @Test
    fun deleteConversation_should_delete_conversation() = runTest {
        // When
        conversationViewModel.deleteConversation(conversationFixture)

        // Then
        coVerify { deleteConversationUseCase.execute(conversationFixture, userFixture.id) }
    }

    @Test
    fun recreateConversation_should_recreate_conversation() = runTest {
        // When
        conversationViewModel.recreateConversation(conversationFixture)

        // Then
        coVerify { recreateConversationUseCase.execute(conversationFixture, userFixture.id) }
    }
}