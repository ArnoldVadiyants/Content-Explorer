package com.example.contentexplorer.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.contentexplorer.feature.home.presentation.HomeEffect
import com.example.contentexplorer.feature.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoute(
    onOpenImage: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is HomeEffect.OpenImage -> onOpenImage(effect.imageId)
            }
        }
    }

    HomeScreen(state = state, onAction = viewModel::onAction, modifier = modifier)
}
