package com.example.contentexplorer.feature.imagedetail.presentation

sealed interface ImageDetailEffect {
    data object NavigateBack : ImageDetailEffect
}
