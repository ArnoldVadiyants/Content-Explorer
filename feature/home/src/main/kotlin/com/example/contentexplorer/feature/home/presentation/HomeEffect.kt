package com.example.contentexplorer.feature.home.presentation

sealed interface HomeEffect {
    data class OpenImage(
        val imageId: Long,
    ) : HomeEffect
}
