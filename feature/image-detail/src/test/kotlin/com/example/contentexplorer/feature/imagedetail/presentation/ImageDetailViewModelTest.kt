package com.example.contentexplorer.feature.imagedetail.presentation

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.core.domain.repository.ContentRepository
import com.example.contentexplorer.feature.imagedetail.domain.usecase.GetImageDetailUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class ImageDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: ContentRepository

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads the requested image on first collection`() = runTest(dispatcher) {
        Dispatchers.setMain(dispatcher)
        val image = ImageQuestion(id = 7, src = "image.png", title = "Image")
        coEvery { repository.getImage(7) } returns image
        val viewModel = ImageDetailViewModel(7, GetImageDetailUseCase(repository))

        viewModel.uiState.test {
            val loaded = awaitState { it.image != null && !it.isLoading }
            assertEquals(image, loaded.image)
            assertEquals(false, loaded.error)
        }
    }

    @Test
    fun `back action emits navigation effect`() = runTest(dispatcher) {
        Dispatchers.setMain(dispatcher)
        coEvery { repository.getImage(7) } returns null
        val viewModel = ImageDetailViewModel(7, GetImageDetailUseCase(repository))

        viewModel.uiState.test {
            runCurrent()
            viewModel.effects.test {
                viewModel.onAction(ImageDetailAction.Back)
                assertEquals(ImageDetailEffect.NavigateBack, awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun ReceiveTurbine<ImageDetailUiState>.awaitState(
        predicate: (ImageDetailUiState) -> Boolean,
    ): ImageDetailUiState {
        repeat(10) {
            val state = awaitItem()
            if (predicate(state)) return state
        }
        error("Expected state was not emitted")
    }
}
