package com.example.contentexplorer.feature.home.presentation

import com.example.contentexplorer.core.domain.model.Page

data class HomeUiState(
    val data: List<Page> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: HomeError? = null,
    val selectedResponses: Map<Long, Set<Long>> = emptyMap(),
)
