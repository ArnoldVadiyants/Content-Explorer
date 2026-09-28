package com.example.contentexplorer.feature.imagedetail.presentation

sealed interface ImageDetailAction {
    data object Back : ImageDetailAction
    data object Retry : ImageDetailAction
}
