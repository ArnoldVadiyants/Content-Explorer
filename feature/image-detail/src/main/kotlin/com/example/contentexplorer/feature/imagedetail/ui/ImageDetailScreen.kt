package com.example.contentexplorer.feature.imagedetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.contentexplorer.feature.imagedetail.presentation.ImageDetailAction
import com.example.contentexplorer.feature.imagedetail.presentation.ImageDetailUiState

@Composable
fun ImageDetailScreen(
    state: ImageDetailUiState,
    onAction: (ImageDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val image = state.image
    when {
        image != null -> ImageDetailContent(image = image, modifier = modifier)
        state.error -> ImageDetailErrorContent(
            onRetry = { onAction(ImageDetailAction.Retry) },
            modifier = modifier,
        )
        else -> ImageDetailLoadingContent(modifier = modifier)
    }
}
