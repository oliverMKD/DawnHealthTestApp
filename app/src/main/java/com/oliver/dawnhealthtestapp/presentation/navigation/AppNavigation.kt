package com.oliver.dawnhealthtestapp.presentation.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.oliver.dawnhealthtestapp.presentation.screens.NavGraphs
import com.oliver.dawnhealthtestapp.presentation.viewmodel.GitHubRepositoriesViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency

@Composable
fun AppNavigation(
    activity: ComponentActivity,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    DestinationsNavHost(
        navController = navController,
        navGraph = NavGraphs.root,
        dependenciesContainerBuilder = {
            dependency(NavGraphs.homeGraph){
                val parentEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry(NavGraphs.homeGraph.route)
                }
                hiltViewModel<GitHubRepositoriesViewModel>(parentEntry)
            }
            dependency(hiltViewModel<GitHubRepositoriesViewModel>(activity))
        }
    )
}