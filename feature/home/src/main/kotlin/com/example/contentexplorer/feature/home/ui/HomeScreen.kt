package com.example.contentexplorer.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.contentexplorer.feature.home.presentation.HomeAction
import com.example.contentexplorer.feature.home.presentation.HomeUiState

@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.data.isEmpty() && state.isLoading ->
            HomeLoadingContent(modifier = modifier)

        state.data.isEmpty() && state.error != null ->
            HomeErrorContent(
                error = state.error,
                onRetry = { onAction(HomeAction.Refresh) },
                modifier = modifier,
            )

        state.data.isEmpty() ->
            HomeEmptyContent(modifier = modifier)

        else ->
            HomeContent(state = state, onAction = onAction, modifier = modifier)
    }
}
