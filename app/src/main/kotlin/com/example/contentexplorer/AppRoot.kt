package com.example.contentexplorer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.contentexplorer.feature.home.navigation.Home
import com.example.contentexplorer.feature.home.presentation.HomeAction
import com.example.contentexplorer.feature.home.presentation.HomeViewModel
import com.example.contentexplorer.feature.home.ui.HomeRoute
import com.example.contentexplorer.feature.home.ui.HomeTopBar
import org.koin.compose.viewmodel.koinViewModel

/**
 * Root composable setting up top bars and Navigation3 backstack routing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val backStack = rememberNavBackStack(Home)
    val currentDestination = backStack.lastOrNull()

    Scaffold(
        topBar = {
            when (currentDestination) {
                Home -> {
                    val homeViewModel: HomeViewModel = koinViewModel()
                    HomeTopBar(onRefresh = { homeViewModel.onAction(HomeAction.Refresh) })
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.padding(innerPadding),
            entryProvider = entryProvider {
                entry<Home> {
                    HomeRoute(onOpenImage = { imageId -> { /*TODO*/ } })
                }
            },
        )
    }
}
