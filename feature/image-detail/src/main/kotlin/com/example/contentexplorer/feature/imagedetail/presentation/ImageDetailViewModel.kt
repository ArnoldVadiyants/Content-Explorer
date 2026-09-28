package com.example.contentexplorer.feature.imagedetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.feature.imagedetail.domain.usecase.GetImageDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

/**
 * ViewModel for loading and presenting details for a specific image item.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class ImageDetailViewModel(
    @InjectedParam
    private val imageId: Long,
    private val getImageDetailUseCase: GetImageDetailUseCase,
) : ViewModel() {

    private val actions = MutableSharedFlow<ImageDetailAction>(extraBufferCapacity = 64)

    fun onAction(action: ImageDetailAction) {
        actions.tryEmit(action)
    }

    private val _effects = MutableSharedFlow<ImageDetailEffect>(extraBufferCapacity = 8)
    val effects: SharedFlow<ImageDetailEffect> = _effects.asSharedFlow()

    val uiState: StateFlow<ImageDetailUiState> = actions
        .onStart { emit(ImageDetailAction.Retry) }
        .flatMapMerge { action -> handleAction(action) }
        .scan(ImageDetailUiState()) { state, change -> state.reduce(change) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ImageDetailUiState(),
        )

    private fun handleAction(action: ImageDetailAction): Flow<ActionStateChange> = flow {
        when (action) {
            ImageDetailAction.Back -> {
                _effects.emit(ImageDetailEffect.NavigateBack)
            }

            ImageDetailAction.Retry -> {
                emit(ActionStateChange.LoadStarted)
                val image = getImageDetailUseCase(imageId)
                if (image != null) {
                    emit(ActionStateChange.LoadSuccess(image))
                } else {
                    emit(ActionStateChange.LoadFailed)
                }
            }
        }
    }

    private sealed interface ActionStateChange {
        data object LoadStarted : ActionStateChange
        data class LoadSuccess(val image: ImageQuestion) : ActionStateChange
        data object LoadFailed : ActionStateChange
    }

    private fun ImageDetailUiState.reduce(change: ActionStateChange): ImageDetailUiState = when (change) {
        ActionStateChange.LoadStarted -> copy(isLoading = true, error = false)
        is ActionStateChange.LoadSuccess -> copy(image = change.image, isLoading = false, error = false)
        ActionStateChange.LoadFailed -> copy(image = null, isLoading = false, error = true)
    }
}
