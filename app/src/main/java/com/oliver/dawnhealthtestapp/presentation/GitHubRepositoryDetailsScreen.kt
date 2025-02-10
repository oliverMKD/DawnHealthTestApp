package com.oliver.dawnhealthtestapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.presentation.navigation.HomeGraph
import com.oliver.dawnhealthtestapp.presentation.viewmodel.GitHubRepositoriesViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@HomeGraph
@Destination
@Composable
fun GitHubRepositoryDetailsScreen(
    modifier: Modifier = Modifier,
    repositoryItem: GithubRepository,
    navController: DestinationsNavigator,
    viewModel: GitHubRepositoriesViewModel,
){

}