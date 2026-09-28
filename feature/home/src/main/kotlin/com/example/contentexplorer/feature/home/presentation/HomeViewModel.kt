package com.example.contentexplorer.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentexplorer.core.domain.model.ChoiceQuestion
import com.example.contentexplorer.core.domain.model.ContentItem
import com.example.contentexplorer.core.domain.model.Page
import com.example.contentexplorer.core.domain.model.Section
import com.example.contentexplorer.feature.home.domain.usecase.ObserveContentUseCase
import com.example.contentexplorer.feature.home.domain.usecase.RefreshContentUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import org.koin.core.annotation.KoinViewModel
import java.io.IOException

/**
 * ViewModel managing Home screen state, actions, and UI side effects.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class HomeViewModel(
    observeContentUseCase: ObserveContentUseCase,
    private val refreshContentUseCase: RefreshContentUseCase,
) : ViewModel() {

    private val actions = MutableSharedFlow<HomeAction>(extraBufferCapacity = 64)

    fun onAction(action: HomeAction) {
        actions.tryEmit(action)
    }

    private val _effects = MutableSharedFlow<HomeEffect>(extraBufferCapacity = 8)
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    // Mutex prevents concurrent refresh network calls
    private val refreshMutex = Mutex()

    private val contentState: Flow<List<Page>> = observeContentUseCase().onStart { emit(emptyList()) }

    // Transforms incoming UI actions into state mutations using scan as a state reducer
    private val actionState: Flow<ActionState> = actions
        .onStart { emit(HomeAction.Refresh) }
        .flatMapMerge { action -> handleAction(action) }
        .scan(ActionState()) { state, change -> state.reduce(change) }

    val uiState: StateFlow<HomeUiState> = combine(
        contentState,
        actionState,
    ) { pages, actions ->
        HomeUiState(
            data = pages,
            isLoading = actions.isRefreshing && pages.isEmpty(),
            isRefreshing = actions.isRefreshing && pages.isNotEmpty(),
            error = actions.error,
            selectedResponses = actions.selectedResponses,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeUiState(),
    )

    private fun handleAction(action: HomeAction): Flow<ActionStateChange> = flow {
        when (action) {
            HomeAction.Refresh -> emitAll(handleRefresh())

            is HomeAction.ImageClicked -> {
                _effects.emit(HomeEffect.OpenImage(action.imageId))
            }

            is HomeAction.ResponseSelected -> {
                uiState.value.data.findChoiceQuestion(action.questionId)?.let {
                    emit(ActionStateChange.ResponseSelected(it, action.responseId))
                }
            }
        }
    }

    private fun handleRefresh(): Flow<ActionStateChange> = flow {
        // Ensure only one refresh request runs at a time
        if (refreshMutex.tryLock()) {
            try {
                emit(ActionStateChange.RefreshStarted)
                refreshContentUseCase()
                    .onFailure { throwable -> emit(ActionStateChange.RefreshFailed(throwable.toHomeError())) }
                emit(ActionStateChange.RefreshFinished)
            } finally {
                refreshMutex.unlock()
            }
        }
    }

    private data class ActionState(
        val isRefreshing: Boolean = false,
        val error: HomeError? = null,
        val selectedResponses: Map<Long, Set<Long>> = emptyMap(),
    ) {
        fun reduce(change: ActionStateChange): ActionState = when (change) {
            ActionStateChange.RefreshStarted -> copy(isRefreshing = true, error = null)
            ActionStateChange.RefreshFinished -> copy(isRefreshing = false)
            is ActionStateChange.RefreshFailed -> copy(error = change.error)
            is ActionStateChange.ResponseSelected -> copy(
                selectedResponses = selectedResponses.applySelection(change),
            )
        }

        // Toggles selection for multiple-selection questions or replaces for single selection
        private fun Map<Long, Set<Long>>.applySelection(
            change: ActionStateChange.ResponseSelected,
        ): Map<Long, Set<Long>> {
            val questionId = change.question.id
            // Retrieve existing selected response IDs for this question
            val current = this[questionId].orEmpty()
            val updated = if (change.question.responseSet.multipleSelection) {
                // Toggle response ID for multi-select
                if (change.responseId in current) current - change.responseId else current + change.responseId
            } else {
                // Replace with single response ID for single-select
                setOf(change.responseId)
            }
            // Update mapping of question IDs to selected response IDs
            return this + (questionId to updated)
        }
    }

    private sealed interface ActionStateChange {
        data object RefreshStarted : ActionStateChange
        data object RefreshFinished : ActionStateChange

        data class RefreshFailed(
            val error: HomeError,
        ) : ActionStateChange

        data class ResponseSelected(
            val question: ChoiceQuestion,
            val responseId: Long,
        ) : ActionStateChange
    }
}

private fun Throwable.toHomeError(): HomeError = when (this) {
    is IOException -> HomeError.Network
    else -> HomeError.Unknown
}

// Recursively searches for a choice question by ID across pages and nested sections
private fun List<Page>.findChoiceQuestion(questionId: Long): ChoiceQuestion? {
    fun ContentItem.find(): ChoiceQuestion? = when (this) {
        is ChoiceQuestion -> takeIf { id == questionId }
        is Section -> items.firstNotNullOfOrNull { it.find() }
        else -> null
    }
    for (page in this) {
        page.items.firstNotNullOfOrNull { it.find() }?.let { return it }
    }
    return null
}
