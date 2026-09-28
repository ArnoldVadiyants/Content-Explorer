package com.example.contentexplorer.feature.home.presentation

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.example.contentexplorer.core.domain.model.ChoiceQuestion
import com.example.contentexplorer.core.domain.model.Page
import com.example.contentexplorer.core.domain.model.Response
import com.example.contentexplorer.core.domain.model.ResponseSet
import com.example.contentexplorer.core.domain.model.Section
import com.example.contentexplorer.core.domain.repository.ContentRepository
import com.example.contentexplorer.feature.home.domain.usecase.ObserveContentUseCase
import com.example.contentexplorer.feature.home.domain.usecase.RefreshContentUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: ContentRepository

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `response selection toggles a nested multiple choice question`() = runTest(dispatcher) {
        Dispatchers.setMain(dispatcher)
        val question = ChoiceQuestion(
            id = 10,
            content = "Pick",
            responseSet = ResponseSet(
                id = 11,
                multipleSelection = true,
                responses = listOf(Response(12, "One", null)),
            ),
        )
        val pages = listOf(Page(1, "Page", listOf(Section(2, "Section", listOf(question)))))
        every { repository.observePages() } returns MutableStateFlow(pages)
        coEvery { repository.refresh() } returns Result.success(Unit)
        val viewModel = HomeViewModel(ObserveContentUseCase(repository), RefreshContentUseCase(repository))

        viewModel.uiState.test {
            awaitState { it.data == pages }

            viewModel.onAction(HomeAction.ResponseSelected(question.id, 12))
            assertEquals(setOf(12L), awaitState { it.selectedResponses.containsKey(question.id) }.selectedResponses[question.id])

            viewModel.onAction(HomeAction.ResponseSelected(question.id, 12))
            assertEquals(emptySet<Long>(), awaitState { it.selectedResponses[question.id] == emptySet<Long>() }.selectedResponses[question.id])
        }
    }

    @Test
    fun `image click emits an open image effect`() = runTest(dispatcher) {
        Dispatchers.setMain(dispatcher)
        every { repository.observePages() } returns MutableStateFlow(emptyList())
        coEvery { repository.refresh() } returns Result.success(Unit)
        val viewModel = HomeViewModel(ObserveContentUseCase(repository), RefreshContentUseCase(repository))

        viewModel.uiState.test {
            runCurrent()
            viewModel.effects.test {
                viewModel.onAction(HomeAction.ImageClicked(42))
                assertEquals(HomeEffect.OpenImage(42), awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun ReceiveTurbine<HomeUiState>.awaitState(
        predicate: (HomeUiState) -> Boolean,
    ): HomeUiState {
        repeat(10) {
            val state = awaitItem()
            if (predicate(state)) return state
        }
        error("Expected state was not emitted")
    }
}
