package com.example.contentexplorer.feature.home.presentation

sealed interface HomeAction {
    data object Refresh : HomeAction

    data class ResponseSelected(
        val questionId: Long,
        val responseId: Long,
    ) : HomeAction

    data class ImageClicked(
        val imageId: Long,
    ) : HomeAction
}
