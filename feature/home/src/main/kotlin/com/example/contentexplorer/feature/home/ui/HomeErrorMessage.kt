package com.example.contentexplorer.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.contentexplorer.feature.home.R
import com.example.contentexplorer.feature.home.presentation.HomeError

@Composable
fun HomeError.message(): String = when (this) {
    HomeError.Network -> stringResource(R.string.home_error_network)
    HomeError.Unknown -> stringResource(R.string.home_error_unknown)
}
