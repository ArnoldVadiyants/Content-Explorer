package com.example.contentexplorer.feature.imagedetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.contentexplorer.feature.imagedetail.presentation.ImageDetailEffect
import com.example.contentexplorer.feature.imagedetail.presentation.ImageDetailViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ImageDetailRoute(
    imageId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageDetailViewModel = koinViewModel(key = imageId.toString()) { parametersOf(imageId) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ImageDetailEffect.NavigateBack -> onBack()
            }
        }
    }

    ImageDetailScreen(state = state, onAction = viewModel::onAction, modifier = modifier)
}
