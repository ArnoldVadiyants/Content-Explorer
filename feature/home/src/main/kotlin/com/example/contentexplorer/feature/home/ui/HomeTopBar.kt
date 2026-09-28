package com.example.contentexplorer.feature.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.contentexplorer.feature.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onRefresh: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.home_title)) },
        actions = {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.content_refresh),
                )
            }
        },
    )
}
