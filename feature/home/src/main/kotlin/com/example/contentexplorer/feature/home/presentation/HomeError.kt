package com.example.contentexplorer.feature.home.presentation

sealed interface HomeError {
    data object Network : HomeError
    data object Unknown : HomeError
}
