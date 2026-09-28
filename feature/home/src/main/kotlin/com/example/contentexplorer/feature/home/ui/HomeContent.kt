package com.example.contentexplorer.feature.home.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.contentexplorer.core.designsystem.theme.Spacing
import com.example.contentexplorer.feature.home.presentation.HomeAction
import com.example.contentexplorer.feature.home.presentation.HomeError
import com.example.contentexplorer.feature.home.presentation.HomeUiState
import com.example.contentexplorer.feature.home.ui.components.PageContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onAction(HomeAction.Refresh) },
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.md),
        ) {
            state.error?.let { error ->
                item(key = "error-banner") {
                    HomeErrorBanner(error = error)
                }
            }
            items(state.data, key = { it.id }) { page ->
                PageContent(
                    page = page,
                    selectedResponses = state.selectedResponses,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun HomeErrorBanner(error: HomeError, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.md),
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(
            text = error.message(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(Spacing.md),
        )
    }
}
