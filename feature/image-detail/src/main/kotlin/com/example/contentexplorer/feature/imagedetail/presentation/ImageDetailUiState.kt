package com.example.contentexplorer.feature.imagedetail.presentation

import com.example.contentexplorer.core.domain.model.ImageQuestion

data class ImageDetailUiState(
    val image: ImageQuestion? = null,
    val isLoading: Boolean = true,
    val error: Boolean = false,
)
